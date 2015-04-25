package com.spygame.managers;

//modified code Based on a Google Play Services 
//turn based multiplayer sample called TMPBSkeleton
//Wolff (wolff@google.com), 2013


import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.turnbased.OnTurnBasedMatchUpdateReceivedListener;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig;
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.spygame.R;

public class MultiplayerManager extends BaseGameActivity implements
		OnInvitationReceivedListener, OnTurnBasedMatchUpdateReceivedListener {

	private AlertDialog alerts;

	// For our intents
	final static int RC_SELECT_PLAYERS = 1;
	final static int RC_LOOK_AT_MATCHES = 11;

	final static int TOAST_DELAY = 2000;

	public boolean performingTurn = false;

	public TurnBasedMatch currentMatch;

	public GameState mGameState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiplayer_manager);

		// Setup signin button
		findViewById(R.id.sign_out_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						signOut();
						setViewVisibility();
					}
				});

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// start the asynchronous sign in flow
						beginUserInitiatedSignIn();

						findViewById(R.id.sign_in_button).setVisibility(
								View.GONE);

					}
				});
	}

	// Open the create-game UI.
	public void onStartMatchClicked(View view) {
		Intent intent = Games.TurnBasedMultiplayer.getSelectOpponentsIntent(
				getApiClient(), 1, 2, true);
		startActivityForResult(intent, RC_SELECT_PLAYERS);
	}

	// Create a one-on-one automatch game.
	public void onQuickMatchClicked(View view) {

		Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(1, 1, 0);

		TurnBasedMatchConfig tbmc = TurnBasedMatchConfig.builder()
				.setAutoMatchCriteria(autoMatchCriteria).build();

		showSpinner();

		// Start the match
		ResultCallback<TurnBasedMultiplayer.InitiateMatchResult> cb = new ResultCallback<TurnBasedMultiplayer.InitiateMatchResult>() {
			@Override
			public void onResult(TurnBasedMultiplayer.InitiateMatchResult result) {
				processResult(result);
			}
		};
		Games.TurnBasedMultiplayer.createMatch(getApiClient(), tbmc)
				.setResultCallback(cb);
	}

	public void onCancelClicked(View view) {
		showSpinner();
		Games.TurnBasedMultiplayer.cancelMatch(getApiClient(),
				currentMatch.getMatchId()).setResultCallback(
				new ResultCallback<TurnBasedMultiplayer.CancelMatchResult>() {
					@Override
					public void onResult(
							TurnBasedMultiplayer.CancelMatchResult result) {
						processResult(result);
					}
				});
		performingTurn = false;
		setViewVisibility();
	}


	// Upload your new gamestate, then take a turn, and pass it on to the next
	// player.
	public void onDoneClicked(View view) {
		showSpinner();

		String nextParticipantId = getNextParticipantId();
		// Create the next turn

		showSpinner();

		Games.TurnBasedMultiplayer
				.takeTurn(getApiClient(), currentMatch.getMatchId(),
						mGameState.persist(), nextParticipantId)
				.setResultCallback(
						new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {
							@Override
							public void onResult(
									TurnBasedMultiplayer.UpdateMatchResult result) {
								processResult(result);
							}
						});

		mGameState = null;
	}

	// Sign-in, Sign out behavior

	// Update the visibility based on what state we're in.
	public void setViewVisibility() {
		if (!isSignedIn()) {
			findViewById(R.id.login_layout).setVisibility(View.VISIBLE);
			findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
			findViewById(R.id.matchup_layout).setVisibility(View.GONE);
			findViewById(R.id.gameplay_layout).setVisibility(View.GONE);

			if (alerts != null) {
				alerts.dismiss();
			}
			return;
		}

		((TextView) findViewById(R.id.name_field)).setText(Games.Players
				.getCurrentPlayer(getApiClient()).getDisplayName());
		findViewById(R.id.login_layout).setVisibility(View.GONE);

		if (performingTurn) {
			findViewById(R.id.matchup_layout).setVisibility(View.GONE);
			findViewById(R.id.gameplay_layout).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.matchup_layout).setVisibility(View.VISIBLE);
			findViewById(R.id.gameplay_layout).setVisibility(View.GONE);
		}
	}

	@Override
	public void onSignInFailed() {
		setViewVisibility();
	}

	@Override
	public void onSignInSucceeded() {
		if (mHelper.getTurnBasedMatch() != null) {
			// GameHelper will cache any connection hint it gets. In this case,
			// it can cache a TurnBasedMatch that it got from choosing a
			// turn-based
			// game notification. If that's the case, you should go straight
			// into
			// the game.
			updateMatch(mHelper.getTurnBasedMatch());
			return;
		}

		setViewVisibility();

		// As a demonstration, we are registering this activity as a handler for
		// invitation and match events.

		// This is *NOT* required; if you do not register a handler for
		// invitation events, you will get standard notifications instead.
		// Standard notifications may be preferable behavior in many cases.
		Games.Invitations.registerInvitationListener(getApiClient(), this);

		// Likewise, we are registering the optional MatchUpdateListener, which
		// will replace notifications you would get otherwise. You do *NOT* have
		// to register a MatchUpdateListener.
		Games.TurnBasedMultiplayer.registerMatchUpdateListener(getApiClient(),
				this);
	}

	// Switch to gameplay view.
	public void setGameplayUI() {
		performingTurn = true;
		setViewVisibility();

	}

	// Helpful dialogs

	public void showSpinner() {
		findViewById(R.id.progressLayout).setVisibility(View.VISIBLE);
	}

	public void dismissSpinner() {
		findViewById(R.id.progressLayout).setVisibility(View.GONE);
	}



	// Rematch dialog
	public void askForRematch() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setMessage("Do you want a rematch?");

		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Sure, rematch!",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								rematch();
							}
						})
				.setNegativeButton("No.",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
							}
						});

		alertDialogBuilder.show();
	}

	// This function is what gets called when you return from either the Play
	// Games built-in inbox, or else the create game built-in interface.
	@Override
	public void onActivityResult(int request, int response, Intent data) {
		// It's VERY IMPORTANT for you to remember to call your superclass.
		// BaseGameActivity will not work otherwise.
		super.onActivityResult(request, response, data);

        if (request == RC_SELECT_PLAYERS) {
            if (response != Activity.RESULT_OK) {
                // user canceled
                return;
            }


            // get the invitee list
            final ArrayList<String> invitees =
                    data.getStringArrayListExtra(Multiplayer.EXTRA_INVITATION);

            // get auto-match criteria
            Bundle autoMatchCriteria = null;
            int minAutoMatchPlayers = data.getIntExtra(
                    Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
            int maxAutoMatchPlayers
                    = data.getIntExtra(
                    Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);
            if (minAutoMatchPlayers > 0) {
                autoMatchCriteria
                        = RoomConfig.createAutoMatchCriteria(
                        minAutoMatchPlayers, maxAutoMatchPlayers, 0);
            } else {
                autoMatchCriteria = null;
            }


			TurnBasedMatchConfig tbmc = TurnBasedMatchConfig.builder()
					.addInvitedPlayers(invitees)
					.setAutoMatchCriteria(autoMatchCriteria).build();

			// Start the match
			Games.TurnBasedMultiplayer
					.createMatch(getApiClient(), tbmc)
					.setResultCallback(
							new ResultCallback<TurnBasedMultiplayer.InitiateMatchResult>() {
								@Override
								public void onResult(
										TurnBasedMultiplayer.InitiateMatchResult result) {
									processResult(result);
								}
							});
			showSpinner();
		}
	}

	// startMatch() happens in response to the createTurnBasedMatch()
	// above. This is only called on success, so we should have a
	// valid match object. We're taking this opportunity to setup the
	// game, saving our initial state. Calling takeTurn() will
	// callback to OnTurnBasedMatchUpdated(), which will show the game
	// UI.
	public void startMatch(TurnBasedMatch match) {
		mGameState = new GameState();
		// Some basic turn data

		currentMatch = match;

		String playerId = Games.Players.getCurrentPlayerId(getApiClient());
		String myParticipantId = currentMatch.getParticipantId(playerId);

		showSpinner();

		Games.TurnBasedMultiplayer.takeTurn(getApiClient(), match.getMatchId(),
				mGameState.persist(), myParticipantId).setResultCallback(
				new ResultCallback<TurnBasedMultiplayer.UpdateMatchResult>() {
					@Override
					public void onResult(
							TurnBasedMultiplayer.UpdateMatchResult result) {
						processResult(result);
					}
				});
	}

	// If you choose to rematch, then call it and wait for a response.
	public void rematch() {
		showSpinner();
		Games.TurnBasedMultiplayer
				.rematch(getApiClient(), currentMatch.getMatchId())
				.setResultCallback(
						new ResultCallback<TurnBasedMultiplayer.InitiateMatchResult>() {
							@Override
							public void onResult(
									TurnBasedMultiplayer.InitiateMatchResult result) {
								processResult(result);
							}
						});
		currentMatch = null;
		performingTurn = false;
	}

	/**
	 * Get the next participant. In this function, we assume that we are
	 * round-robin, with all known players going before all automatch players.
	 * This is not a requirement; players can go in any order. However, you can
	 * take turns in any order.
	 * 
	 * @return participantId of next player, or null if automatching
	 */
	public String getNextParticipantId() {

		String playerId = Games.Players.getCurrentPlayerId(getApiClient());
		String myParticipantId = currentMatch.getParticipantId(playerId);

		ArrayList<String> participantIds = currentMatch.getParticipantIds();

		int desiredIndex = -1;

		for (int i = 0; i < participantIds.size(); i++) {
			if (participantIds.get(i).equals(myParticipantId)) {
				desiredIndex = i + 1;
			}
		}

		if (desiredIndex < participantIds.size()) {
			return participantIds.get(desiredIndex);
		}

		if (currentMatch.getAvailableAutoMatchSlots() <= 0) {
			// You've run out of automatch slots, so we start over.
			return participantIds.get(0);
		} else {
			// You have not yet fully automatched, so null will find a new
			// person to play against.
			return null;
		}
	}

	// This is the main function that gets called when players choose a match
	// from the inbox, or else create a match and want to start it.
	public void updateMatch(TurnBasedMatch match) {
		currentMatch = match;

		int status = match.getStatus();
		int turnStatus = match.getTurnStatus();

		mGameState = null;

		setViewVisibility();
	}

	private void processResult(TurnBasedMultiplayer.CancelMatchResult result) {
		dismissSpinner();

		performingTurn = false;
	}

	private void processResult(TurnBasedMultiplayer.InitiateMatchResult result) {
		TurnBasedMatch match = result.getMatch();
		dismissSpinner();

		if (match.getData() != null) {
			// This is a game that has already started, so I'll just start
			updateMatch(match);
			return;
		}

		startMatch(match);
	}

	public void processResult(TurnBasedMultiplayer.UpdateMatchResult result) {
		TurnBasedMatch match = result.getMatch();
		dismissSpinner();
		if (match.canRematch()) {
			askForRematch();
		}

		performingTurn = (match.getTurnStatus() == TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN);

		if (performingTurn) {
			updateMatch(match);
			return;
		}

		setViewVisibility();
	}

	// Handle notification events.
	@Override
	public void onInvitationReceived(Invitation invitation) {
		Toast.makeText(
				this,
				"An invitation has arrived from "
						+ invitation.getInviter().getDisplayName(), TOAST_DELAY)
				.show();
	}

	@Override
	public void onInvitationRemoved(String invitationId) {
		Toast.makeText(this, "An invitation was removed.", TOAST_DELAY).show();
	}

	@Override
	public void onTurnBasedMatchReceived(TurnBasedMatch match) {
		Toast.makeText(this, "A match was updated.", TOAST_DELAY).show();
	}

	@Override
	public void onTurnBasedMatchRemoved(String matchId) {
		Toast.makeText(this, "A match was removed.", TOAST_DELAY).show();
	}
}