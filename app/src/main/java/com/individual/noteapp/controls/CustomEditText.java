package com.individual.noteapp.controls;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Blackout on 1/25/2017.
 */

public class CustomEditText extends EditText {


    public enum TextStyle{
        UNDER_LINE_STYLE,BOLD_STYLE,ITALIC_STYLE
    }

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean changeTextStyle(TextStyle style) {

        if (getSelectionEnd() != getSelectionStart()) {

            SpannableString span = new SpannableString(getText());

            int endSelection = getSelectionEnd();

            switch (style) {
                case BOLD_STYLE: {
                    span.setSpan(new StyleSpan(Typeface.BOLD),getSelectionStart(), getSelectionEnd() , 0);
                }
                break;
                case ITALIC_STYLE: {
                    span.setSpan(new StyleSpan(Typeface.ITALIC),getSelectionStart(), getSelectionEnd() , 0);
                }
                break;
                case UNDER_LINE_STYLE: {
                    span.setSpan(new UnderlineSpan(),getSelectionStart(), getSelectionEnd() , 0);
                }
                break;
            }

            setText(span, EditText.BufferType.SPANNABLE);
            setSelection(endSelection);

            return true;
        } else {
            return false;
        }
    }



    public String getFinalText() {
        return Html.toHtml(getText());
    }



}







