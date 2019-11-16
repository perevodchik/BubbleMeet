package com.perevodchik.bubblemeet.ui.mainmenu.bubble

import android.annotation.SuppressLint
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AbsoluteLayout
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment

import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.data.model.UserData
import com.perevodchik.bubblemeet.ui.mainmenu.MainActivity
import com.perevodchik.bubblemeet.ui.user.UserPreviewFragment
import com.perevodchik.bubblemeet.util.Values
import com.squareup.picasso.Picasso

import java.util.ArrayList
import java.util.Calendar

import de.hdodenhof.circleimageview.CircleImageView
import kotlin.math.*

class BubbleFragment0 : Fragment() {
    private var isConnectBottom: Boolean = false
    private var isConnectLeft: Boolean = false
    private var isConnectRight: Boolean = false
    private var isConnectTop: Boolean = false
    /* access modifiers changed from: private */
    private var isMoving: Boolean = false
    private var isRightSideMoving: Boolean = false
    /* access modifiers changed from: private */
    private var isSlowed: Boolean = false
    private var isTopSideMoving: Boolean = false
    private var isXMoving: Boolean = false
    private var isYMoving: Boolean = false
    private var mDefaultBubbleDiameter: Int = 10
    private var mDefaultYMaxSize: IntArray? = null
    private var mDefaultYMinSize: IntArray? = null
    /* access modifiers changed from: private */
    private var mDiameterPrevious: IntArray? = null
    /* access modifiers changed from: private */
    private var mDifferenceX: Int = 0
    /* access modifiers changed from: private */
    private var mDifferenceY: Int = 0
    private var mDisplayCenterX: Int = 0
    private var mDisplayCenterY: Int = 0
    //private Filter mFilter;
    /* access modifiers changed from: private */
    private lateinit var layout: AbsoluteLayout
    private var mMultiplies: DoubleArray? = null
    private val mPresenter = BubblePresenter()
    /* access modifiers changed from: private */
    private var mPreviousMoveTime: Long = 0
    private var mRows: Int = 0
    private var mSize: Point? = null
    /* access modifiers changed from: private */
    private var mStartClickTime: Long = 0
    private val mUsers = ArrayList<UserData>()
    /* access modifiers changed from: private */
    private var mXPrevious: IntArray? = null
    /* access modifiers changed from: private */
    private var mYPrevious: IntArray? = null

    private val defaultDiffMultiplier: Int = 1

    fun setUsers(list: List<UserData>) {
        Log.d("setUsers", "+")
        this.mUsers.addAll(list)

        Log.d("Users", mUsers.toString())

        this.mDefaultYMinSize = IntArray(this.mUsers.size)
        this.mDefaultYMaxSize = IntArray(this.mUsers.size)
        this.mXPrevious = IntArray(this.mUsers.size)
        this.mYPrevious = IntArray(this.mUsers.size)
        this.mDiameterPrevious = IntArray(this.mUsers.size)
        this.mMultiplies = DoubleArray(this.mUsers.size)
        initViews()
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bubble_grid, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.layout = view.findViewById(R.id.bubble_layout)
        mPresenter.getUsers()
        Log.d("ViewCreated", mUsers.size.toString())
    }

    override fun onStart() {
        super.onStart()
        Log.d("onStart", mUsers.size.toString())
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViews() {
        Log.d("initViews", "+")
        /*Filter filter = this.mFilter;
        if (filter != null) {
            this.mUsers = this.mPresenter.filter(this.mUsers, filter, getActivity());
        }*/
        val activity = (activity as MainActivity?)!!
        //activity.hideButtonBack();
        //activity.showButtonFilters();
        //activity.resetMenuIcons();
        val display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        this.mSize = size
        val d = size.x.toDouble()
        this.mDefaultBubbleDiameter = (d / 2.15).toInt()
        this.mDisplayCenterX = size.x / 2
        val i = this.mDisplayCenterX
        this.mDisplayCenterX = i - i / 21
        this.mDisplayCenterY = size.y / 2 - size.y / 10
        this.mRows = sqrt(this.mUsers.size.toDouble()).toInt()
        Log.d("sqrt", mRows.toString())
        Log.d("centerX", mDisplayCenterX.toString())
        Log.d("centerY", mDisplayCenterY.toString())

        for (i2 in 0 until this.mRows) {
            for (j in 0 until this.mRows) {
                val imageViewBubble = CircleImageView(context)
                val i3 = this.mDefaultBubbleDiameter
                val d2 = size.x.toDouble()
                val i4 = (d2 / 2.45).toInt() * j
                val d3 = size.y.toDouble()
                val params = AbsoluteLayout.LayoutParams(i3 + 50, i3 + 50, i4, (d3 / 4.9).toInt() * i2)

                if (i2 % 2 != 0) {
                    val i5 = params.x
                    val d4 = size.x.toDouble()
                    params.x = i5 + (d4 / 2.45).toInt() / 2
                }

                imageViewBubble.layoutParams = params
                layout.addView(imageViewBubble)
            }
        }

        for (i6 in 0 until layout.childCount) {
            //println("get child at $i6")
            layout.getChildAt(i6).setOnClickListener {
                //Log.d("click", "click at bubble")
                val transaction = fragmentManager!!.beginTransaction()
                val profileFragment = UserPreviewFragment(mUsers[i6], fragmentManager!!)
                transaction.replace(R.id.container, profileFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
            val url = Values.imgUrl + "/" + mUsers[i6].avatarSmall
            //Log.d("load", "load img $url")
            Picasso.with(context).load(url).into(layout.getChildAt(i6) as CircleImageView)

        }
        /*if (this.mFilter == null) {
            Log.d(str, "mFilter+");
            for (int i7 = 0; i7 < this.layout.getChildCount(); i7++) {
                LayoutParams paramsBubbleNew = (LayoutParams) this.layout.getChildAt(i7).getLayoutParams();
                paramsBubbleNew.x += this.mDefaultBubbleDiameter * ((-this.mRows) / 2);
                paramsBubbleNew.y += this.mDefaultBubbleDiameter * ((-this.mRows) / 4);
            }
        } else {
            if (this.layout.getChildCount() == 1) {
                for (int i8 = 0; i8 < this.layout.getChildCount(); i8++) {
                    LayoutParams paramsBubbleNew2 = (LayoutParams) this.layout.getChildAt(i8).getLayoutParams();
                    paramsBubbleNew2.x += this.mDisplayCenterX - (this.mDefaultBubbleDiameter / 2);
                    paramsBubbleNew2.y += this.mDisplayCenterY;
                }
            }
            Log.d(str, "mFilter-");
        }*/

        for (i9 in 0 until layout.childCount) {
            val paramsBubble = layout.getChildAt(i9).layoutParams as AbsoluteLayout.LayoutParams
            this.mDefaultYMaxSize!![i9] = paramsBubble.y - this.mDefaultBubbleDiameter / 2
            this.mDefaultYMinSize!![i9] = paramsBubble.y
        }

        //for (i10 in 0..9) {
            start()
        //}

        Log.d("bubbles", mUsers.size.toString())

        layout.setOnTouchListener(View.OnTouchListener { _, event ->
            for (i1 in 0 until layout.childCount) {
                val paramsBubble =
                    layout
                        .getChildAt(i1)
                        .layoutParams as AbsoluteLayout.LayoutParams

                Log.d("Layout touch", "${event.action}")

                when (event.action) {
                    0 -> {
                        isMoving = false
                        mStartClickTime = Calendar.getInstance().timeInMillis
                        mPreviousMoveTime = Calendar.getInstance().timeInMillis
                        println("event.action = 0")
                    }
                    1 -> {
                        println("event.action = 1")
                        if (Calendar.getInstance().timeInMillis - mStartClickTime < 75) {
                            isBubble(event)
                        }
                        if (Calendar.getInstance().timeInMillis - mPreviousMoveTime < 20) {
                            isMoving = true
                            mDifferenceX /= 2
                            mDifferenceY /= 2
                            if (abs(mDifferenceX) > 2 && abs(mDifferenceY) > 2) {
                                if (mDifferenceX > 40) {
                                    mDifferenceX = 40
                                } else if (mDifferenceX in 0..14) {
                                    mDifferenceX = 15
                                }
                                if (mDifferenceY > 40) {
                                    mDifferenceY = 40
                                } else if (mDifferenceY in 0..14) {
                                    mDifferenceY = 15
                                }
                                if (mDifferenceX < -40) {
                                    mDifferenceX = -40
                                } else if (mDifferenceX > -15 && mDifferenceX <= 0) {
                                    mDifferenceX = -15
                                }
                                if (mDifferenceY < -40) {
                                    mDifferenceY = -40
                                } else if (mDifferenceY > -15 && mDifferenceY <= 0) {
                                    mDifferenceY = -15
                                }
                            }
                            inertia()
                        }
                        return@OnTouchListener true
                    }
                    2 -> {
                        println("event.action = 2")
                        mPreviousMoveTime = Calendar.getInstance().timeInMillis
                        update(i1, paramsBubble, event)
                    }
                }
                mXPrevious!![i1] = event.x.roundToInt()
                this.mYPrevious!![i1] = event.y.roundToInt()
                this.mDiameterPrevious!![i1] = paramsBubble.height
            }
            Log.d(NotificationCompat.CATEGORY_EVENT, event.toString())
            true
        })
    }

    private fun inertia() {
        val myHandler = Handler()
        myHandler.postDelayed(object : Runnable {
            override fun run() {
                if (isMoving) {
                    if (mDifferenceX == 0 && mDifferenceY == 0) {
                        isMoving = false
                    }
                    if (mDifferenceX < 0 && mDifferenceY > 0) {
                        if (isSlowed) {
                            mDifferenceX += defaultDiffMultiplier
                            mDifferenceY -= defaultDiffMultiplier
                            isSlowed = false
                        } else {
                            isSlowed = true
                        }
                    }
                    if (mDifferenceX < 0 && mDifferenceY < 0) {
                        if (isSlowed) {
                            mDifferenceX += defaultDiffMultiplier
                            mDifferenceY += defaultDiffMultiplier
                            isSlowed = false
                        } else {
                            isSlowed = true
                        }
                    }
                    if (mDifferenceX > 0 && mDifferenceY > 0) {
                        if (isSlowed) {
                            mDifferenceX -= defaultDiffMultiplier
                            mDifferenceY -= defaultDiffMultiplier
                            isSlowed = false
                        } else {
                            isSlowed = true
                        }
                    }
                    if (mDifferenceX > 0 && mDifferenceY < 0) {
                        if (isSlowed) {
                            mDifferenceX -= defaultDiffMultiplier
                            mDifferenceY += defaultDiffMultiplier
                            isSlowed = false
                        } else {
                            isSlowed = true
                        }
                    }
                    if (mDifferenceX > 0 && mDifferenceY == 0) {
                        if (isSlowed) {
                            mDifferenceX -= defaultDiffMultiplier
                            isSlowed = false
                        } else {
                            isSlowed = true
                        }
                    }
                    if (mDifferenceX < 0 && mDifferenceY == 0) {
                        if (isSlowed) {
                            mDifferenceX += defaultDiffMultiplier
                            isSlowed = false
                        } else {
                            isSlowed = true
                        }
                    }
                    if (mDifferenceY > 0 && mDifferenceX == 0) {
                        if (isSlowed) {
                            mDifferenceY -= defaultDiffMultiplier
                            isSlowed = false
                        } else {
                            isSlowed = true
                        }
                    }
                    if (mDifferenceY < 0 && mDifferenceX == 0) {
                        if (isSlowed) {
                            mDifferenceY += defaultDiffMultiplier
                            isSlowed = false
                        } else {
                            isSlowed = true
                        }
                    }
                    start()
                    myHandler.postDelayed(this, 5)
                }
            }
        }, 5)
    }

    private fun update(i: Int, paramsBubble: AbsoluteLayout.LayoutParams, event: MotionEvent) {
        val pixelsToSideFromBubbleX: Float
        val pixelsToSideFromBubbleY: Float
        val isGoingTop: Boolean
        var toSideFromBubblePercentY: Float
        val toSideFromBubblePercentX: Float
        var multiply: Float
        val iArr = this.mXPrevious
        if (iArr!![i] > 0 && iArr[i] < this.mDisplayCenterX * 2 && iArr[i] != 0 && this.mYPrevious!![i] != 0) {
            this.mDifferenceX = (event.x - this.mXPrevious!![i].toFloat()).roundToInt()
            this.mDifferenceY = (event.y - this.mYPrevious!![i].toFloat()).roundToInt()
            if (this.mDifferenceX < 0) {
                this.isRightSideMoving = true
                this.isXMoving = true
            } else {
                this.isRightSideMoving = false
                this.isXMoving = true
            }
            if (this.mDifferenceY > 0) {
                this.isTopSideMoving = true
                this.isYMoving = true
            } else {
                this.isTopSideMoving = false
                this.isYMoving = true
            }
            val str = ""
            if (i == 6) {
                val sb = StringBuilder()
                sb.append(this.mDifferenceX)
                sb.append(str)
                Log.d("differenceX", sb.toString())
                val sb2 = StringBuilder()
                sb2.append(this.mDifferenceY)
                sb2.append(str)
                val str2 = "differenceY"
                Log.d(str2, sb2.toString())
                Log.d(str2, " ")
            }
            val paramsLeftTopBorderBubble =
                layout.getChildAt(0).layoutParams as AbsoluteLayout.LayoutParams
            this.isConnectLeft = paramsLeftTopBorderBubble.x > this.mDefaultBubbleDiameter / 2
            this.isConnectTop = paramsLeftTopBorderBubble.y > this.mDefaultBubbleDiameter
            val paramsRightBottomBorderBubble =
                layout.getChildAt(layout.childCount - 1).layoutParams as AbsoluteLayout.LayoutParams
            this.isConnectRight = paramsRightBottomBorderBubble.x < this.mDisplayCenterX - this.mDefaultBubbleDiameter / 2
            this.isConnectBottom = paramsRightBottomBorderBubble.y < this.mDisplayCenterY
            if (this.isConnectBottom || this.isConnectRight || this.isConnectTop || this.isConnectLeft) {
                if (this.isConnectBottom && this.isTopSideMoving) {
                    val i3 = paramsBubble.y
                    val i4 = this.mDifferenceY
                    paramsBubble.y = i3 + i4
                    val iArr2 = this.mDefaultYMinSize
                    iArr2!![i] = iArr2[i] + i4
                }
                if (this.isConnectRight && !this.isRightSideMoving) {

                    paramsBubble.x += this.mDifferenceX
                }
                if (this.isConnectTop && !this.isTopSideMoving) {
                    val i5 = paramsBubble.y
                    val i6 = this.mDifferenceY
                    paramsBubble.y = i5 + i6
                    val iArr3 = this.mDefaultYMinSize
                    iArr3!![i] = iArr3[i] + i6
                }
                if (this.isConnectLeft && this.isRightSideMoving) {
                    paramsBubble.x += this.mDifferenceX
                }
            } else {
                val f = paramsBubble.x.toFloat() + this.mDefaultBubbleDiameter.toFloat() / 2.5f
                val i7 = this.mDisplayCenterX
                if (f < i7.toFloat()) {
                    pixelsToSideFromBubbleX =
                        paramsBubble.x.toFloat() + this.mDefaultBubbleDiameter.toFloat() / 2.5f
                } else {
                    pixelsToSideFromBubbleX =
                        (i7 * 2 - paramsBubble.x).toFloat() - this.mDefaultBubbleDiameter.toFloat() / 2.5f
                }
                val i8 = paramsBubble.y
                val i9 = this.mDisplayCenterY
                if (i8 < i9 - this.mDefaultBubbleDiameter / 3 * 2) {
                    isGoingTop = true
                    pixelsToSideFromBubbleY = paramsBubble.y.toFloat()
                } else {
                    isGoingTop = false
                    pixelsToSideFromBubbleY =
                        (i9 * 2 - paramsBubble.y - this.mDefaultBubbleDiameter / 2).toFloat()
                }
                if (pixelsToSideFromBubbleX <= 0.0f || pixelsToSideFromBubbleY <= 0.0f) {
                    toSideFromBubblePercentX = 0.0f
                    toSideFromBubblePercentY = 0.0f
                } else {
                    toSideFromBubblePercentX =
                        pixelsToSideFromBubbleX / this.mDisplayCenterX.toFloat()
                    if (isGoingTop) {
                        val i10 = this.mDisplayCenterY
                        toSideFromBubblePercentY =
                            pixelsToSideFromBubbleY / (i10 - i10 / 2).toFloat()
                        if (toSideFromBubblePercentY < 0.0f) {
                            toSideFromBubblePercentY = 0.0f
                        }
                    } else {
                        toSideFromBubblePercentY =
                            pixelsToSideFromBubbleY / this.mDisplayCenterY.toFloat()
                    }
                }
                if (toSideFromBubblePercentX > toSideFromBubblePercentY) {
                    multiply = toSideFromBubblePercentY
                    if (toSideFromBubblePercentY < 1.0f) {
                        if (toSideFromBubblePercentY.toDouble() > 0.5) {
                            paramsBubble.height =
                                (abs(toSideFromBubblePercentY) * this.mDefaultBubbleDiameter.toFloat()).toInt()
                        } else {
                            paramsBubble.height =
                                (abs(toSideFromBubblePercentY) * this.mDefaultBubbleDiameter.toFloat()).toInt()
                        }
                    } else if (toSideFromBubblePercentY < 0.0f) {
                        paramsBubble.height = 0
                    } else {
                        paramsBubble.height = this.mDefaultBubbleDiameter
                    }
                } else {
                    multiply = toSideFromBubblePercentX
                    if (toSideFromBubblePercentX < 1.0f) {
                        if (toSideFromBubblePercentX.toDouble() > 0.5) {
                            paramsBubble.height =
                                (abs(toSideFromBubblePercentX) * this.mDefaultBubbleDiameter.toFloat()).toInt()
                        } else {
                            paramsBubble.height =
                                (abs(toSideFromBubblePercentX) * this.mDefaultBubbleDiameter.toFloat()).toInt()
                        }
                    } else if (toSideFromBubblePercentX < 0.0f) {
                        paramsBubble.height = 0
                    } else {
                        paramsBubble.height = this.mDefaultBubbleDiameter
                    }
                }
                if (multiply > 1.0f) {
                    multiply = 1.0f
                }
                this.mMultiplies!![i] = multiply.toDouble()
                paramsBubble.x += this.mDifferenceX
                val i11 = paramsBubble.y
                val i12 = this.mDifferenceY
                paramsBubble.y = i11 + i12
                val iArr4 = this.mDefaultYMinSize
                iArr4!![i] = iArr4[i] + i12
                val iArr5 = this.mDefaultYMaxSize
                iArr5!![i] = iArr5[i] + i12
                if (multiply > 0.2f && multiply < 1.0f) {
                    paramsBubble.y =
                        (iArr4[i].toFloat() - this.mDefaultBubbleDiameter.toFloat() * multiply / 2.0f).toInt()
                    if (i == 6) {
                        val sb3 = StringBuilder()
                        sb3.append(multiply)
                        sb3.append(str)
                        Log.d("multiply", sb3.toString())
                    }
                }
                if (multiply == 0.0f) {
                    if (i == 0 && layout.childCount > 1) {
                        val params =
                            layout.getChildAt(0).layoutParams as AbsoluteLayout.LayoutParams
                        val params2 =
                            layout.getChildAt(this.mRows).layoutParams as AbsoluteLayout.LayoutParams
                        val iArr6 = this.mDefaultYMinSize
                        params.y = iArr6!![1]
                        iArr6[0] = iArr6[1]
                        val i13 = params2.x
                        val d = this.mSize!!.x.toDouble()
                        java.lang.Double.isNaN(d)
                        params.x = i13 - (d / 2.45).toInt() / 2
                        if (params.height != 0) {
                            params.y += (this.mDiameterPrevious!![0] - params.height) / 2
                        }
                        layout.getChildAt(0).layoutParams = params
                    }
                    paramsBubble.y = this.mDefaultYMinSize!![i]
                }
                if (multiply == 1.0f) {
                    paramsBubble.y = this.mDefaultYMaxSize!![i]
                }
            }
        }
        layout.getChildAt(i).layoutParams = paramsBubble
    }

    private fun isBubble(event: MotionEvent) {
        /*for (i in 0 until layout.childCount) {
            val paramsBubble =
                layout.getChildAt(i).layoutParams as AbsoluteLayout.LayoutParams
            val x = event.x.toDouble()
            val d = paramsBubble.x.toDouble()
            val d2 = this.mDefaultBubbleDiameter.toDouble()
            val d3 = 1.0 - this.mMultiplies!![i]
            val d4 = d2 * d3 / 2.0
            if (x > d + d4 && event.y > paramsBubble.y.toFloat()) {
                val x2 = event.x.toDouble()
                val d5 = (paramsBubble.x + paramsBubble.height).toDouble()
                val d6 = this.mDefaultBubbleDiameter.toDouble()
                val d7 = 1.0 - this.mMultiplies!![i]
                val d8 = d6 * d7 / 2.0
                if (x2 < d5 + d8 && event.y < (paramsBubble.y + paramsBubble.height).toFloat()) {
                    Log.d("sizeWidth", paramsBubble.width.toString())
                    Log.d("sizeHeight", paramsBubble.height.toString())
                    return
                }
            }
        }*/
    }

    private fun start() {
        var pixelsToSideFromBubbleX: Float
        var pixelsToSideFromBubbleY: Float
        var isGoingTop: Boolean
        var toSideFromBubblePercentY: Float
        var toSideFromBubblePercentX: Float
        var multiply: Float
        var i: Int
        for (i2 in 0 until layout.childCount) {
            val paramsBubble =
                layout.getChildAt(i2).layoutParams as AbsoluteLayout.LayoutParams
            if (i2 == 6) {
                Log.d("differenceX", mDifferenceX.toString())
                Log.d( "differenceY", mDifferenceY.toString())
            }
            val paramsLeftTopBorderBubble =
                layout.getChildAt(0).layoutParams as AbsoluteLayout.LayoutParams
            this.isConnectLeft = paramsLeftTopBorderBubble.x > this.mDefaultBubbleDiameter / 2
            this.isConnectTop = paramsLeftTopBorderBubble.y > this.mDefaultBubbleDiameter
            val paramsRightBottomBorderBubble =
                layout.getChildAt(layout.childCount - 1).layoutParams as AbsoluteLayout.LayoutParams
            this.isConnectRight = paramsRightBottomBorderBubble.x < this.mDisplayCenterX - this.mDefaultBubbleDiameter / 2
            this.isConnectBottom = paramsRightBottomBorderBubble.y < this.mDisplayCenterY
            if (this.isConnectBottom || this.isConnectRight || this.isConnectTop || this.isConnectLeft) {
                if (this.isConnectBottom && this.isTopSideMoving) {
                    val i3 = paramsBubble.y
                    val i4 = this.mDifferenceY
                    paramsBubble.y = i3 + i4
                    val iArr = this.mDefaultYMinSize
                    iArr!![i2] = iArr[i2] + i4
                }
                if (this.isConnectRight && !this.isRightSideMoving) {
                    paramsBubble.x += this.mDifferenceX
                }
                if (this.isConnectTop && !this.isTopSideMoving) {
                    val i5 = paramsBubble.y
                    val i6 = this.mDifferenceY
                    paramsBubble.y = i5 + i6
                    val iArr2 = this.mDefaultYMinSize
                    iArr2!![i2] = iArr2[i2] + i6
                }
                if (this.isConnectLeft && this.isRightSideMoving) {
                    paramsBubble.x += this.mDifferenceX
                }
            } else {
                val f = paramsBubble.x.toFloat() + this.mDefaultBubbleDiameter.toFloat() / 2.5f
                val i7 = this.mDisplayCenterX
                pixelsToSideFromBubbleX = if (f < i7.toFloat()) {
                    paramsBubble.x.toFloat() + this.mDefaultBubbleDiameter.toFloat() / 2.5f
                } else {
                    (i7 * 2 - paramsBubble.x).toFloat() - this.mDefaultBubbleDiameter.toFloat() / 2.5f
                }
                val i8 = paramsBubble.y
                val i9 = this.mDisplayCenterY
                if (i8 < i9 - this.mDefaultBubbleDiameter / 3 * 2) {
                    isGoingTop = true
                    pixelsToSideFromBubbleY = paramsBubble.y.toFloat()
                } else {
                    isGoingTop = false
                    pixelsToSideFromBubbleY =
                        (i9 * 2 - paramsBubble.y - this.mDefaultBubbleDiameter / 2).toFloat()
                }
                if (pixelsToSideFromBubbleX <= 0.0f || pixelsToSideFromBubbleY <= 0.0f) {
                    toSideFromBubblePercentX = 0.0f
                    toSideFromBubblePercentY = 0.0f
                } else {
                    toSideFromBubblePercentX =
                        pixelsToSideFromBubbleX / this.mDisplayCenterX.toFloat()
                    if (isGoingTop) {
                        val i10 = this.mDisplayCenterY
                        toSideFromBubblePercentY =
                            pixelsToSideFromBubbleY / (i10 - i10 / 2).toFloat()
                        if (toSideFromBubblePercentY < 0.0f) {
                            toSideFromBubblePercentY = 0.0f
                        }
                    } else {
                        toSideFromBubblePercentY =
                            pixelsToSideFromBubbleY / this.mDisplayCenterY.toFloat()
                    }
                }
                if (toSideFromBubblePercentX > toSideFromBubblePercentY) {
                    multiply = toSideFromBubblePercentY
                    if (toSideFromBubblePercentY < 1.0f) {
                        if (toSideFromBubblePercentY.toDouble() > 0.5) {
                            paramsBubble.height =
                                (abs(toSideFromBubblePercentY) * this.mDefaultBubbleDiameter.toFloat()).toInt()
                        } else {
                            paramsBubble.height =
                                (abs(toSideFromBubblePercentY) * this.mDefaultBubbleDiameter.toFloat()).toInt()
                        }
                    } else {
                        if (toSideFromBubblePercentY < 0.0f) {
                            paramsBubble.height = 0
                        } else {
                            paramsBubble.height = this.mDefaultBubbleDiameter
                        }
                    }
                } else {
                    multiply = toSideFromBubblePercentX
                    if (toSideFromBubblePercentX < 1.0f) {
                        if (toSideFromBubblePercentX.toDouble() > 0.5) {
                            paramsBubble.height =
                                (abs(toSideFromBubblePercentX) * this.mDefaultBubbleDiameter.toFloat()).toInt()
                        } else {
                            paramsBubble.height =
                                (abs(toSideFromBubblePercentX) * this.mDefaultBubbleDiameter.toFloat()).toInt()
                        }
                    } else if (toSideFromBubblePercentX < 0.0f) {
                        paramsBubble.height = 0
                    } else {
                        paramsBubble.height = this.mDefaultBubbleDiameter
                    }
                }
                if (multiply > 1.0f) {
                    multiply = 1.0f
                }
                this.mMultiplies!![i2] = multiply.toDouble()
                paramsBubble.x += this.mDifferenceX
                val i11 = paramsBubble.y
                val i12 = this.mDifferenceY
                paramsBubble.y = i11 + i12
                val iArr3 = this.mDefaultYMinSize
                iArr3!![i2] = iArr3[i2] + i12
                val iArr4 = this.mDefaultYMaxSize
                iArr4!![i2] = iArr4[i2] + i12
                if (multiply > 0.0f && multiply < 1.0f) {
                    paramsBubble.y =
                        (iArr3[i2].toFloat() - this.mDefaultBubbleDiameter.toFloat() * multiply / 2.0f).toInt()
                    if (i2 == 6) {
                        Log.d("multiply", multiply.toString())
                    }
                }
                if (multiply == 0.0f) {
                    paramsBubble.y = this.mDefaultYMinSize!![i2]
                    if (i2 != 0 || layout.childCount <= 1) {
                    } else {
                        val params =
                            layout.getChildAt(0).layoutParams as AbsoluteLayout.LayoutParams
                        val params2 =
                            layout.getChildAt(this.mRows).layoutParams as AbsoluteLayout.LayoutParams
                        val iArr5 = this.mDefaultYMinSize
                        params.y = iArr5!![1]
                        iArr5[0] = iArr5[1]
                        val i13 = params2.x
                        val d = this.mSize!!.x.toDouble()
                        params.x = i13 - (d / 2.45).toInt() / 2
                        if (params.height != 0) {
                            i = 0
                            params.y += (this.mDiameterPrevious!![0] - params.height) / 2
                        } else {
                            i = 0
                        }
                        layout.getChildAt(i).layoutParams = params
                    }
                }
                if (multiply == 1.0f) {
                    paramsBubble.y = this.mDefaultYMaxSize!![i2]
                }
            }
            layout.getChildAt(i2).layoutParams = paramsBubble
            this.mDiameterPrevious!![i2] = paramsBubble.height
        }
    }

    companion object {
        private val MAX_CLICK_DURATION = 75
        private val MAX_MOVEMENT_DIFFERENCE = 20
    }

}
