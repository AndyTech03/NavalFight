package com.example.navalfight;

import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.ColorRes;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.List;


public class GameMapFragment extends Fragment {

    public interface OnCellClickedListener {
        void onEvent(Point cell);
    }

    public static final Size MAP_SIZE = new Size(10, 10);

    OnCellClickedListener cellClickedListener;

    public void setOnCellClickedListener(OnCellClickedListener listener) {
        cellClickedListener = listener;
    }

    private final ImageView[][] cells = new ImageView[MAP_SIZE.getWidth()][MAP_SIZE.getHeight()];

    public GameMapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ViewGroup rows = (ViewGroup) view;
        for (int y = 0; y < MAP_SIZE.getHeight(); y++) {
            ViewGroup row = (ViewGroup) rows.getChildAt(y);
            for (int x = 0; x < MAP_SIZE.getWidth(); x++) {
                cells[x][y] = (ImageView) row.getChildAt(x);
                final Point pos = new Point(x, y);
                cells[x][y].setOnClickListener(l -> cellClickedListener.onEvent(pos));
            }
        }
    }

    public void clear() {
        for (int y = 0; y < MAP_SIZE.getHeight(); y++) {
            for (int x = 0; x < MAP_SIZE.getWidth(); x++) {
                cells[x][y].setImageResource(R.color.white);
            }
        }
    }

    public void drawShips(List<Point> decks) {
        for (Point p : decks) {
            cells[p.x][p.y].setImageResource(R.color.my_ship);
        }
    }

    public void drawShoots(List<Shoot> shoots) {
        for (Shoot shoot : shoots) {
            Point p = shoot.getTarget();
            @ColorRes int id = R.color.white;
            switch (shoot.getResult()) {
                case Hit:
                    id = R.color.hitted_ship;
                    break;
                case Miss:
                    id = R.color.miss_shoot;
                    break;
                case Invalid:
                    continue;
            }
            cells[p.x][p.y].setImageResource(id);
        }
    }

    public void drawDestroyed(List<Ship> destroyedShips) {
        for (Ship ship : destroyedShips)
            for (Point p : ship.getDecksLocations())
                cells[p.x][p.y].setImageResource(R.color.destroyed_ship);
    }

    public void drawPlaceableShip(List<Point> allowed_decks, List<Point> not_allowed_decks) {
        for (Point p : allowed_decks) {
            cells[p.x][p.y].setImageResource(R.color.placeable_ship_allowed);
        }
        for (Point p : not_allowed_decks) {
            cells[p.x][p.y].setImageResource(R.color.placeable_ship_not_allowed);
        }
    }
}