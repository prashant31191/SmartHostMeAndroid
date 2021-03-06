package com.smarthost.ui.views.avenir;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import com.smarthost.R;

/**
 * User: davidredding
 * Date: 3/4/14
 * Time: 3:32 PM
 */
public class AvenirEditText extends EditText {

    /**
     * Simple constructor to use when creating a widget from code.
     *
     * @param context The Context the widget is running in, through which it can
     *                access the current theme, resources, etc.
     */
    public AvenirEditText(Context context) {
        super(context);
        onInitTypeface(context, null, 0);
    }

    /**
     * Constructor that is called when inflating a widget from XML. This is called
     * when a widget is being constructed from an XML file, supplying attributes
     * that were specified in the XML file. This version uses a default style of
     * 0, so the only attribute values applied are those in the Context's Theme
     * and the given AttributeSet.
     * <p/>
     * <p/>
     * The method onFinishInflate() will be called after all children have been
     * added.
     *
     * @param context The Context the widget is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the widget.
     * @see #AvenirEditText(android.content.Context, android.util.AttributeSet, int)
     */
    public AvenirEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        onInitTypeface(context, attrs, 0);
    }

    /**
     * Perform inflation from XML and apply a class-specific base style. This
     * constructor of View allows subclasses to use their own base style when
     * they are inflating.
     *
     * @param context  The Context the widget is running in, through which it can
     *                 access the current theme, resources, etc.
     * @param attrs    The attributes of the XML tag that is inflating the widget.
     * @param defStyle The default style to apply to this widget. If 0, no style
     *                 will be applied (beyond what is included in the theme). This may
     *                 either be an attribute resource, whose value will be retrieved
     *                 from the current theme, or an explicit style resource.
     * @see #AvenirEditText(android.content.Context, android.util.AttributeSet)
     */
    public AvenirEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        onInitTypeface(context, attrs, defStyle);
    }

    /**
     * Setup  typeface.
     *
     * @param context  The Context the widget is running in, through which it can
     *                 access the current theme, resources, etc.
     * @param attrs    The attributes of the XML tag that is inflating the widget.
     * @param defStyle The default style to apply to this widget. If 0, no style
     *                 will be applied (beyond what is included in the theme).
     */
    private void onInitTypeface(Context context, AttributeSet attrs, int defStyle) {
        // Typeface.createFromAsset doesn't work in the layout editor, so skipping.
        if (isInEditMode()) {
            return;
        }

        int typefaceValue = 0;
        if (attrs != null) {
            TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.AvenirEditText, defStyle, 0);
            typefaceValue = values.getInt(R.styleable.AvenirEditText_typeface, AvenirTypeFaceManager.DEFAULT);
            values.recycle();
        }

        Typeface nexaTypeface = AvenirTypeFaceManager.obtainTypeface(context, typefaceValue);
        setTypeface(nexaTypeface);
    }

}
