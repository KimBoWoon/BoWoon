package com.bowoon.commonutils

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan

/**
 * 밑줄 친 문자열로 변환하여 반환
 * @param text 문자열
 * @param start 시작 위치
 * @param end 끝 위치
 * @return 적용된 SpannableString 문자열 반환
 */
fun setUnderline(
    text: CharSequence,
    start: Int = 0,
    end: Int = text.length,
    flags: Int = Spannable.SPAN_INCLUSIVE_INCLUSIVE
): SpannableString =
    SpannableString(text).apply {
        setSpan(UnderlineSpan(), start, end, flags)
    }

/**
 * 색상이 적용된 문자열로 변환하여 반환
 * @param text 문자열
 * @param start 시작 위치
 * @param end 끝 위치
 * @return 적용된 SpannableString 문자열 반환
 */
fun setTextColor(
    text: CharSequence,
    color: Int,
    start: Int = 0,
    end: Int = text.length,
    flags: Int = Spannable.SPAN_INCLUSIVE_INCLUSIVE
): SpannableString =
    SpannableString(text).apply {
        setSpan(ForegroundColorSpan(color), start, end, flags)
    }

/**
 * 텍스트 스타일이 적용된 문자열로 변환하여 반환
 * @param text 밑줄치고싶은 문자열
 * @param start 시작 위치
 * @param end 끝 위치
 * @return 적용된 SpannableString 문자열 반환
 */
fun setTextStyle(
    text: CharSequence,
    type: Int,
    start: Int = 0,
    end: Int = text.length,
    flags: Int = Spannable.SPAN_INCLUSIVE_INCLUSIVE
): SpannableString =
    SpannableString(text).apply {
        setSpan(StyleSpan(type), start, end, flags)
    }

/**
 * 밑줄 친 문자열로 변환하여 반환
 * @param text 문자열
 * @param start 시작 위치
 * @param end 끝 위치
 * @return 적용된 SpannableString 문자열 반환
 */
fun CharSequence.underline(
    start: Int = 0,
    end: Int = this.length,
    flags: Int = Spannable.SPAN_INCLUSIVE_INCLUSIVE
): SpannableString =
    SpannableString(this).apply {
        setSpan(UnderlineSpan(), start, end, flags)
    }

/**
 * 색상이 적용된 문자열로 변환하여 반환
 * @param text 문자열
 * @param start 시작 위치
 * @param end 끝 위치
 * @return 적용된 SpannableString 문자열 반환
 */
fun CharSequence.textColor(
    color: Int,
    start: Int = 0,
    end: Int = this.length,
    flags: Int = Spannable.SPAN_INCLUSIVE_INCLUSIVE
): SpannableString =
    SpannableString(this).apply {
        setSpan(ForegroundColorSpan(color), start, end, flags)
    }

/**
 * 텍스트 스타일이 적용된 문자열로 변환하여 반환
 * @see android.graphics.Typeface
 * @param text 밑줄치고싶은 문자열
 * @param start 시작 위치
 * @param end 끝 위치
 * @return 적용된 SpannableString 문자열 반환
 */
fun CharSequence.textStyle(
    type: Int,
    start: Int = 0,
    end: Int = this.length,
    flags: Int = Spannable.SPAN_INCLUSIVE_INCLUSIVE
): SpannableString =
    SpannableString(this).apply {
        setSpan(StyleSpan(type), start, end, flags)
    }

fun List<String>.textStyle(
    text: String,
    flags: Int = Spannable.SPAN_INCLUSIVE_INCLUSIVE
): SpannableString = SpannableString(text).also {
    forEach { style ->
        when {
            style.equals("BOLD", true) -> it.setSpan(StyleSpan(Typeface.BOLD), 0, text.length, flags)
            style.equals("ITALIC", true) -> it.setSpan(StyleSpan(Typeface.ITALIC), 0, text.length, flags)
            style.equals("UNDERLINE", true) -> it.setSpan(UnderlineSpan(), 0, text.length, flags)
        }
    }
}