package com.suraj.memorygame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suraj.memorygame.Adapters.EasyLevelAdapter;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.TimeZone;


public class EasyLevel extends Fragment {

    //Timer
    TextView tvTimer;
    long startTime;
    private Handler customHandler = new Handler();
    long counterStartTime = 0;

    private RecyclerView EasyLevelRecyclerView;
    private TextView tvEasylevelcounter;
    public ArrayList<Integer> cards;
    public int CARDS[] = {
            R.drawable.facebook,
            R.drawable.instagram,
            R.drawable.linkedin,
            R.drawable.reddit,
            R.drawable.snapchat,
            R.drawable.youtube,
            R.drawable.facebook,
            R.drawable.instagram,
            R.drawable.linkedin,
            R.drawable.reddit,
            R.drawable.snapchat,
            R.drawable.youtube
    };
    EasyFlipView flippedCard;
    public static long RemainingTime = Constants.EASY_TIME;
    public boolean isPaused, isCancelled;
    Bundle b;
    private SharedPreferences pref;
    int pos, count, bestScore;
    private boolean match;


    public EasyLevel() {
        // Required empty public constructor
    }

    public void shuffle(int cards[], int n) {
        Random random = new Random();

        for (int i = 0; i < n; i++) {
            int r = random.nextInt(n - i);

            int temp = cards[r];
            cards[r] = cards[i];
            cards[i] = temp;
        }
    }

    public void fragmentTransaction(Bundle b) {
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        final Result r = new Result();
        r.setArguments(b);
        transaction.replace(R.id.layoutFragment, r);
        transaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_easy_level, container, false);

        tvEasylevelcounter = rootView.findViewById(R.id.easylevelcounter);
        EasyLevelRecyclerView = rootView.findViewById(R.id.easylevelview);
        b = new Bundle();
        b.putInt("level", Constants.LEVEL_EASY);
        pref = getActivity().getSharedPreferences(Constants.PREF_NAME, 0);
        bestScore = pref.getInt(Constants.EASY_HIGH_KEY, (int) (Constants.EASY_TIME / Constants.TIMER_INTERVAL));

        ((TextView) rootView.findViewById(R.id.bestEasy)).append(bestScore + "");

        RecyclerView.LayoutManager lm = new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false);
        EasyLevelRecyclerView.setLayoutManager(lm);

        cards = new ArrayList<>();
        // TODO: card shuffle here

        shuffle(CARDS,CARDS.length);
        shuffle(CARDS, CARDS.length);   // double shuffle
        for (int card : CARDS) {
            cards.add(card);
        }

        EasyLevelRecyclerView.setAdapter(new EasyLevelAdapter(cards));

        isPaused = false;
        isCancelled = false;


        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    isPaused = true;
                    pauseTimer();
                    AlertDialog.Builder pause = new AlertDialog.Builder(getContext());
                    pause.setTitle("Game paused");
                    pause.setMessage("Do you want to quit ?");
                    pause.setCancelable(false);
                    pause.setPositiveButton("Resume", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isPaused = false;
                            startTimer();

                        }
                    });
                    pause.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isCancelled = true;
                            getFragmentManager().popBackStack();
                        }
                    });
                    pause.show();
                    return true;
                }
                return false;
            }
        });

        EasyLevelRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(final RecyclerView rv, MotionEvent e) {
                final View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null) {

                    final int position = rv.getChildAdapterPosition(child);

                    if (flippedCard == null) {
                        flippedCard = (EasyFlipView) child;
                        pos = position;

                    } else {
                        if (pos == position) {
                            flippedCard = null;
                        } else {
                            if (cards.get(pos).equals(cards.get(position))) {
                                ((EasyFlipView) child).setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {

                                    /*If cards Matched*/
                                    @Override
                                    public void onViewFlipCompleted(EasyFlipView easyFlipView, EasyFlipView.FlipState newCurrentSide) {
                                        for (int i = 0; i < EasyLevelRecyclerView.getChildCount(); i++) {
                                            EasyFlipView child1 = (EasyFlipView) EasyLevelRecyclerView.getChildAt(i);
                                            child1.setEnabled(false);
                                        }

                                        /*If cards matched before 5, bring back timer to Zero*/
                                        if(counterStartTime <5)
                                            counterStartTime =0;
                                        else
                                            /*If matched after 5 sec, decrement 5 sec*/
                                            counterStartTime -=5;
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                flippedCard.setVisibility(View.GONE);
                                                child.setVisibility(View.GONE);
                                                child.setEnabled(false);
                                                flippedCard.setEnabled(false);
                                                flippedCard = null;
                                                count += 2;

                                                if (count == CARDS.length) {
                                                    b.putString("Data", "win");
                                                    b.putInt("Time", (int) counterStartTime);
                                                    EasyLevel.this.onDestroy();
                                                }
                                                for (int i = 0; i < EasyLevelRecyclerView.getChildCount(); i++) {
                                                    EasyFlipView child1 = (EasyFlipView) EasyLevelRecyclerView.getChildAt(i);
                                                    child1.setEnabled(true);
                                                }
                                            }
                                        }, 200);
                                    }
                                });
                            } else {

                                /*If cards don't match*/

                                ((EasyFlipView) child).setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
                                    @Override
                                    public void onViewFlipCompleted(EasyFlipView easyFlipView, EasyFlipView.FlipState newCurrentSide) {
                                        for (int i = 0; i < EasyLevelRecyclerView.getChildCount(); i++) {
                                            EasyFlipView child1 = (EasyFlipView) EasyLevelRecyclerView.getChildAt(i);
                                            child1.setEnabled(false);
                                        }
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                flippedCard.flipTheView();
                                                ((EasyFlipView) child).flipTheView();
                                                flippedCard = null;
                                                ((EasyFlipView) child).setOnFlipListener(null);

                                                for (int i = 0; i < EasyLevelRecyclerView.getChildCount(); i++) {
                                                    EasyFlipView child1 = (EasyFlipView) EasyLevelRecyclerView.getChildAt(i);
                                                    child1.setEnabled(true);
                                                }
                                            }
                                        }, 100);
                                    }
                                });
                            }
                        }

                    }
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        startTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentTransaction(b);
    }
    /* @Override
    public void onFinish() {

    }*/

    private Runnable updateTimerThread = new Runnable() {
        public void run() {

            customHandler.postDelayed(this, 1000);
            counterStartTime++;
            tvEasylevelcounter.setText(getDateFromMillis(counterStartTime));


        }
    };
    public  String getDateFromMillis(long d) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        String string= df.format(d*1000);
        return string;
    }
    public void startTimer() {
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }
    public void pauseTimer(){
        customHandler.removeCallbacks(updateTimerThread);
    }


}

