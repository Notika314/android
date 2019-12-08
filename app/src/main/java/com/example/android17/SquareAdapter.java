package com.example.android17;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.example.android17.model.Piece;

public class SquareAdapter extends BaseAdapter {
    private Context context;
    private Piece[][] board;

    public SquareAdapter(Context c, Piece[][] board) {
        this.context = c;
        this.board = board;
    }


    @Override
    public int getCount() {
        return 64;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        int xPos = position%8;
        int yPos = position/8;
        Piece item = (Piece) board[xPos][yPos];
        return item;
        //before:
        // return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView view;
        if (convertView != null) {
            view = (ImageView) convertView;
            if (view.getHeight() == 0) {
                int size = parent.getWidth()/8;
                view.setLayoutParams(new GridView.LayoutParams(size,size));
            }
        }
        else {
            view = new ImageView(context);
            int size = parent.getWidth()/8;
            view.setLayoutParams(new GridView.LayoutParams(size,size));
            /*
            int col = position/8 %2;
            if (col == 0) {
                if (position%2 == 0) {
                view.setBackgroundColor(Color.parseColor("#D18B46"));
                }
                else {
                view.setBackgroundColor(Color.parseColor("#FECEA0"));
                }
            }
            else {
                if (position%2 == 0) {
                view.setBackgroundColor(Color.parseColor("#FECEA0"));
                }
                else {
                view.setBackgroundColor(Color.parseColor("#D18B46"));
                }
            }
            */
            Piece p = board[position%8][position/8];
            if( p != null) {
                view.setImageResource(context.getResources().getIdentifier(p.toString(),
                        "drawable", context.getPackageName()));
            }
        }
        return view;
    }
}
