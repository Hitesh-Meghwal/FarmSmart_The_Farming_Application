package com.example.farmsmart.Dialog

import android.content.Context

class LoadingUtils {

    companion object {
        private var authLoading: authLoading? = null

        fun showDialog(context: Context?, isCancelable: Boolean) {
            hideDialog()
            if (context != null) {
                try {
                    authLoading = authLoading(context)
                    authLoading?.apply {
                        setCanceledOnTouchOutside(true)
                        setCancelable(isCancelable)
                        show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        fun hideDialog() {
            authLoading?.let { loader ->
                if (loader.isShowing) {
                    try {
                        loader.dismiss()
                        authLoading = null
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}
