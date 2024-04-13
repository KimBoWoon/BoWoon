package com.bowoon.commonutils

import androidx.recyclerview.widget.RecyclerView

fun scrollPercent(recyclerView: RecyclerView): Float =
    (recyclerView.computeVerticalScrollOffset() * 1f / (recyclerView.computeVerticalScrollRange() - recyclerView.computeVerticalScrollExtent())) * 100f

//fun RecyclerView.scrollPercent(): Double =
//    (computeVerticalScrollOffset() * 1.0 / (computeVerticalScrollRange() - computeVerticalScrollExtent())) * 100.0