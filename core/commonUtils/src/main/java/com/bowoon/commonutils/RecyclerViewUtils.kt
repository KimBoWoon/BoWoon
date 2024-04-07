package com.bowoon.commonutils

import androidx.recyclerview.widget.RecyclerView

fun scrollPercent(recyclerView: RecyclerView): Double =
    (recyclerView.computeVerticalScrollOffset() * 1.0 / (recyclerView.computeVerticalScrollRange() - recyclerView.computeVerticalScrollExtent())) * 100.0

//fun RecyclerView.scrollPercent(): Double =
//    (computeVerticalScrollOffset() * 1.0 / (computeVerticalScrollRange() - computeVerticalScrollExtent())) * 100.0