package com.example.floppy.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import com.example.floppy.utils.Extensions.Companion.showKeyboard
import java.util.regex.Pattern

class Extensions {
    companion object {
        /**Put an edittext on listen
         *@param listener lambda to return a string
         * */
        fun EditText.listenerEditText(listener: (String) -> Unit) {
            this.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = listener.invoke(this@listenerEditText.text.trim().toString())
                override fun afterTextChanged(p0: Editable?) {}
            })
        }

        /**
         * Clean the EditText
         * */
        fun EditText.cleanEditText() = this.setText("")

        /**
         * Show message
         * @param resource Xml resource to display
         * */
        fun String.showMessage(context: Context,resource : Int){
            val toast= Toast.makeText(context,this,Toast.LENGTH_SHORT)
            toast.view = LayoutInflater.from(context).inflate(resource,null,false)
            toast.show()
        }

        /**
         * Know if the scroll moved
         * @param listener lambda to return a boolean if scroll up (true) or down (false)
         * */
        fun NestedScrollView.listenerScroll(listener : (Boolean) -> Unit){
            this.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                    listener.invoke(oldScrollY > scrollY)
            }))
        }

        /**
         * Close the keyboard
         * */
        fun  View.hideKeyboard(){
            val input = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            input.hideSoftInputFromWindow(windowToken, 0)
        }

        fun EditText.showKeyboard(){
            val input = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            input.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT);
        }
        /**
         * listen to changes of an edittext
         * */
        fun EditText.listenerFocusChange(listener : (View,Boolean)->Unit){
            this.onFocusChangeListener = View.OnFocusChangeListener { view, isFocus ->
                if(isFocus){
                    this.showKeyboard()
                    listener.invoke(view,false)
                }else{ listener.invoke(view,true)}
            }
        }
        /**
         * Validate user email
         * */
        fun String.validateEmail() : Boolean{
            val pattern = Pattern.compile(
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
            )
            val matcher = pattern.matcher(this)
            return matcher.find()
        }

    }

}