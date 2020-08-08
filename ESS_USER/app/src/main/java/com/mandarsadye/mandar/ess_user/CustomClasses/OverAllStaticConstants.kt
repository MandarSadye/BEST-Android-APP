package com.mandarsadye.mandar.ess_user.CustomClasses

import com.mandarsadye.mandar.ess_user.DataServices.UserInformation
import java.net.InetAddress
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by mandar on 20-03-2018.
 */
class OverAllStaticConstants {
    companion object {
        @JvmStatic
        var isAnonymous : Boolean = false
        var userInformation : UserInformation? = null

        fun checkIntenet() : Boolean{
            try {
                val ipAddress = InetAddress.getByName("google.com")
                return !ipAddress.equals("")
            } catch (e: Exception) {
                return false
            }
        }
        fun DateTime() : String{
            val cal = Calendar.getInstance()
            val sd = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
            return sd.format(cal.time)
        }
    }
}