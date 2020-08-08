package com.mandarsadye.mandar.ess_user.Fragments.Payment


import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.camera.CameraSettings
import com.mandarsadye.mandar.ess_user.Activities.StartActivity
import com.mandarsadye.mandar.ess_user.DataServices.BusNumberData
import com.mandarsadye.mandar.ess_user.Model.BusNumbers

import com.mandarsadye.mandar.ess_user.R


class PaymentMain : Fragment() {
    var v : View? = null
    var mActivity : StartActivity? = null
    var mContext : Context? = null
    var height : Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as StartActivity
        mContext = activity
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_payment_main, container, false)
        mActivity!!.qrView = v!!.findViewById(R.id.qr_scanner_view)
        mActivity!!.permissions()
        v!!.findViewById<TextView>(R.id.askToCapture).setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                height = v!!.findViewById<TextView>(R.id.askToCapture).layoutParams.height
                v!!.findViewById<TextView>(R.id.askToCapture).layoutParams.height = 0
                mActivity!!.qrView!!.layoutParams.height = height
                mActivity!!.qrView!!.visibility = View.VISIBLE
                scan()
            }
        })




        return v
    }

    fun scan(){

        val s = CameraSettings()
        s.requestedCameraId = 0 // front/back/etc
        mActivity!!.qrView!!.barcodeView.cameraSettings = s
        mActivity!!.qrView!!.resume()

        mActivity!!.qrView!!.decodeSingle(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult) {
                Toast.makeText(mContext,result.toString(), Toast.LENGTH_LONG).show()
                v!!.findViewById<TextView>(R.id.askToCapture).layoutParams.height = height
//                mActivity!!.qrView!!.layoutParams.height = 0
                mActivity!!.qrView!!.visibility = View.GONE
                v!!.findViewById<TextView>(R.id.BusNumberScan).text = result.toString().trim()
                pickerfun(result.toString().trim())
            }

            override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
        })
    }

    fun pickerfun(result : String) {
        var a : BusNumbers? = null
        for( i in BusNumberData.ourInstance.list){
            if(result == i.Number)
            {
                a = i
                break
            }
        }
        var b = a!!.Halts.split(" ")



        var np1 = v!!.findViewById<NumberPicker>(R.id.startpicker)
        np1!!.maxValue = b.size -1
        np1!!.minValue = 0
        np1!!.wrapSelectorWheel = false
//        setNumberPickerTextColor(np1!!, ContextCompat.getColor(context,R.color.FanColor))
        np1.setOnValueChangedListener(object : NumberPicker.OnValueChangeListener{
            override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal: Int) {
                v!!.findViewById<TextView>(R.id.BusNumberFare).text = (0).toString()
                picker2pick(newVal,b,a)
            }
        })
        np1.setDisplayedValues(toArray<String>(b))
        np1!!.value = 0
    }

    fun picker2pick(newValue : Int,b : List<String>,a : BusNumbers){
        var np1 = v!!.findViewById<NumberPicker>(R.id.endpicker)
        np1!!.maxValue = b.size -1
        np1!!.minValue = 0
        np1!!.wrapSelectorWheel = false
//        setNumberPickerTextColor(np1!!, ContextCompat.getColor(context,R.color.FanColor))
        np1.setOnValueChangedListener(object : NumberPicker.OnValueChangeListener{
            override fun onValueChange(picker: NumberPicker?, oldVal: Int, newVal1: Int) {
                if (newVal1 < newValue)
                    Toast.makeText(mContext,"This way is not possible",Toast.LENGTH_LONG).show()
                else{
                    var dd = a.Distance_Time_Matrix.split(" ")
                    var j = dd[6*newVal1 + 1].toFloat() - dd[6*newValue + 1].toFloat()
                    v!!.findViewById<TextView>(R.id.BusNumberFare).text = (Math.round(j*10)).toString()
                    var m = (Math.round(j*10))
                    v!!.findViewById<Button>(R.id.ClickToBookTicket).setOnClickListener(object : View.OnClickListener{
                        override fun onClick(v1: View?) {
                            mActivity!!.dbRef!!.child("BESTS").child(a.Number).addListenerForSingleValueEvent(object : ValueEventListener{
                                override fun onDataChange(p0: DataSnapshot?) {
                                    var k = p0!!.getValue(BusNumbers::class.java)
                                    mActivity!!.dbRef!!.child("BESTS").child(a.Number).child("CurrentPassangers").setValue(k!!.CurrentPassangers+1)
                                    mActivity!!.dbRef!!.child("BESTS").child(a.Number).child("CurrentRevenue").setValue(k!!.CurrentRevenue + m)

                                    v!!.findViewById<TextView>(R.id.BusNumberFare)!!.text = (0).toString()
                                    v!!.findViewById<NumberPicker>(R.id.startpicker)!!.value = 0
                                    v!!.findViewById<NumberPicker>(R.id.endpicker)!!.value = 0
                                }

                                override fun onCancelled(p0: DatabaseError?) {
                                    null
                                }
                            })
                        }
                    })
                }
            }
        })
        np1.setDisplayedValues(toArray<String>(b))
        np1!!.value = newValue+1
    }

    fun setNumberPickerTextColor(numberPicker: NumberPicker, color: Int): Boolean {
        val count = numberPicker.childCount
        for (i in 0 until count) {
            val child = numberPicker.getChildAt(i)
            if (child is EditText) {
                try {
                    val selectorWheelPaintField = numberPicker.javaClass
                            .getDeclaredField("mSelectorWheelPaint")
                    selectorWheelPaintField.isAccessible = true
                    (selectorWheelPaintField.get(numberPicker) as Paint).setColor(color)
                    child.setTextColor(color)
                    numberPicker.invalidate()
                    return true
                } catch (e: NoSuchFieldException) {
                    Log.w("setNumberPickerTextColor", e)
                } catch (e: IllegalAccessException) {
                    Log.w("setNumberPickerTextColor", e)
                } catch (e: IllegalArgumentException) {
                    Log.w("setNumberPickerTextColor", e)
                }

            }
        }
        return false
    }



    fun changeFirebase(result : String){

    }
    inline fun <reified T> toArray(list: List<*>): Array<T> {
        return (list as List<T>).toTypedArray()
    }
}
