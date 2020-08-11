package com.flowz.introtooralanguage.recyclerviewlistener

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerItemClickListener(context: Context, recyclerView: RecyclerView, private val mListener: OnItemClickListener?): RecyclerView.OnItemTouchListener {


    private val mGestureDetector: GestureDetector

    interface OnItemClickListener{

        fun onItemClick(view: View, position: Int)
        fun onItemLongClick(view: View?, position: Int)
    }

    init {
        mGestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                val childView = recyclerView.findChildViewUnder(e.x, e.y )

                if (childView != null && mListener !=null){
                    mListener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView))

                }

                return true
            }

            override fun onLongPress(e: MotionEvent) {
                val childView = recyclerView.findChildViewUnder(e.x, e.y )

                if (childView != null && mListener !=null){
                    mListener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView))

                }
            }
        })
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val childView = rv.findChildViewUnder(e.x, e.y)

        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)){
            mListener.onItemClick(childView, rv.getChildAdapterPosition(childView))

        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }
}