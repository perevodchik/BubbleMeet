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
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.data.AnimateWrapper
import com.perevodchik.bubblemeet.data.model.UserData
import com.perevodchik.bubblemeet.ui.user.UserPreviewFragment
import com.perevodchik.bubblemeet.util.Api
import com.perevodchik.bubblemeet.util.Math
import com.perevodchik.bubblemeet.util.Values
import com.perevodchik.bubblemeet.util.distance
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.*
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

class BubbleFragment : Fragment() {
    private lateinit var grid: AbsoluteLayout
    private val api: Api = Api()
    private val disposable = CompositeDisposable()
    /** count rows at the grid */
    private var rows: Int = 0
    /** container for hold size screen */
    private var size: Point = Point()
    private val points: MutableList<Point> = mutableListOf()
    /** list with helper class for animation */
    private val animList: MutableList<AnimateWrapper> = mutableListOf()
    /** display */
    private val display by lazy { activity!!.windowManager.defaultDisplay }
    /** default (start and max) bubble size */
    //private val defaultBubbleSize by lazy { Math.dpToPixel(200.0f, context!!).roundToInt() }
    private val defaultBubbleSize by lazy { Math.dpToPixel(size.x / 5.0f, context!!).roundToInt() }
    /** center of the screen */
    private val center by lazy { Point(size.x / 2, size.y / 2) }
    /** point with coordinates where user up finger */
    private val prevPoint: Point = Point()
    private var startClickMillis: Long = 0
    private var endClickMillis: Long = 0
    private var lastMoveMillis: Long = 0
    /** id of the current animation cycle */
    private var animateId: Long = 0L
    /** next 4 boolean variables is need move bubble`s from some side */
    private var isInertialMoveFromLeft = false
    private var isInertialMoveFromTop = false
    private var isInertialMoveFromRight = false
    private var isInertialMoveFromBottom = false
    private lateinit var objLeftTop: View
    private lateinit var objRight: View
    private lateinit var objBottom: View
    /** if can user move bubble`s position with touch */
    private var isBreakMoving: Boolean = false
    private var isOpenedUser: Boolean = false
    private var defSizeX: Int = 0
    private var defSizeY: Int = 0
    private var sizeX: Int  = 0
    private var sizeY: Int = 0
    private var startX: Int = 0
    private var startY: Int = 0

    private var isInertialMove = false
    private var isSlowed = false
    private var inertialX: Int = 0
    private var inertialY: Int = 0

    companion object {
        fun newInstance() = BubbleFragment()
        private const val minScaleMultiplier: Float = 0.04f
        private const val maxScaleMultiplier: Float = 0.8f
        private val users: MutableList<UserData> = mutableListOf()
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("steep", "onCreateView")
        return inflater.inflate(R.layout.fragment_bubble_grid0, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("steep", "onViewCreated")
        grid = activity!!.findViewById(R.id.grid_bubbles)
        display.getSize(size)
        fetch()
    }

    override fun onStart() {
        Log.d("steep", "onStart")
        scale(animateId++)
        super.onStart()
    }

    override fun onResume() {
        Log.d("steep", "onResume")
        scale(animateId++)
        super.onResume()
    }

    private fun inertia() {
        val myHandler = Handler()
        myHandler.postDelayed(object : Runnable {
            override fun run() {
                if (isInertialMove) {
                    if (inertialX == 0 && inertialY == 0) {
                        isInertialMove = false
                    }
                    if (inertialX < 0 && inertialY > 0) {
                        if (isSlowed) {
                            inertialX += 1
                            inertialY -= 1
                            isSlowed = false
                        } else {
                            isSlowed = true
                        }
                    }
                    if (inertialX < 0 && inertialY < 0) {
                        if (isSlowed) {
                            inertialX += 1
                            inertialY += 1
                            isSlowed = false
                        } else {
                            isSlowed = true
                        }
                    }
                    if (inertialX > 0 && inertialY > 0) {
                        if (isSlowed) {
                            inertialX -= 1
                            inertialY -= 1
                            isSlowed = false
                        } else {
                            isSlowed = true
                        }
                    }
                    if (inertialX > 0 && inertialY < 0) {
                        if (isSlowed) {
                            inertialX -= 1
                            inertialY += 1
                            isSlowed = false
                        } else {
                            isSlowed = true
                        }
                    }
                    if (inertialX > 0 && inertialY == 0) {
                        if (isSlowed) {
                            inertialX -= 1
                            isSlowed = false
                        } else {
                            isSlowed = true
                        }
                    }
                    if (inertialX < 0 && inertialY == 0) {
                        if (isSlowed) {
                            inertialX += 1
                            isSlowed = false
                        } else {
                            isSlowed = true
                        }
                    }
                    if (inertialY > 0 && inertialX == 0) {
                        if (isSlowed) {
                            inertialY -= 1
                            isSlowed = false
                        } else {
                            isSlowed = true
                        }
                    }
                    if (inertialY < 0 && inertialX == 0) {
                        if (isSlowed) {
                            inertialY += 1
                            isSlowed = false
                        } else {
                            isSlowed = true
                        }
                    }
                    Log.d("inertial", "x -> $inertialX !!! y -> $inertialY")
                    move()
                    scale(++animateId)
                    validatePositionAtBorder()
                    myHandler.postDelayed(this, 50)
                }
            }
        }, 50)
    }

    private fun move() {
        val dX = //sqrt(inertialX + 0.1f)
            if(inertialX > 0)
                //sqrt(inertialX + 0.1f) / 2
                inertialX + 0.0f
            else
                0.0f
        val dY = //sqrt(inertialX + 0.1f)
            if(inertialY > 0)
                //sqrt(inertialY + 0.1f) / 2
                inertialY + 0.0f
            else
                0.0f
        val mX = dX > 0.0f
        val mY = dY > 0.0f
        for(a in animList) {
            a.setCoordinates(a.view.x + dX, a.view.y + dY)
            a.inertialMove(mX, mY)
        }
    }

    /**
     * check if grid out of border
     * if need start "back" animation
     * and blocking user permission to move grid with finger for 500 millis
     */
    private fun validatePositionAtBorder() {
        if (objLeftTop.x > size.x / 4 && !isInertialMoveFromLeft) {
            isBreakMoving = true
            isInertialMoveFromLeft = true

            if (objLeftTop.y > 0)
                isInertialMoveFromTop = true
            if ((objBottom.y + objBottom.height) < size.y)
                isInertialMoveFromBottom = true

            if (isInertialMoveFromTop) {
                val moveY = objLeftTop.y
                for (aw in animList) {
                    aw.setY(aw.view.y - moveY)
                }
            }
            if (isInertialMoveFromBottom) {
                val moveY = objBottom.height
                for (aw in animList) {
                    aw.setY(aw.view.y + moveY)
                }
            }

            for (aw in animList) {
                aw.setX(aw.view.x - (size.x / 4))
            }
        }

        if (objLeftTop.y > /*size.y / 6*/ defaultBubbleSize / 4 && !isInertialMoveFromTop) {
            isBreakMoving = true
            isInertialMoveFromTop = true

            if (objLeftTop.x > 0)
                isInertialMoveFromLeft = true
            if ((objRight.x + objRight.width) < size.x)
                isInertialMoveFromRight = true

            if (objLeftTop.x > 0) {
                val moveX = objLeftTop.x
                for (aw in animList) {
                    aw.setX(aw.view.x - moveX)
                }
            }

            if (objRight.x + objRight.width < size.x) {
                val moveX = size.x - (objRight.width * 1.5).roundToInt()
                for (aw in animList) {
                    aw.setX(aw.view.x + moveX)
                }
            }

            for (aw in animList) {
                aw.setY(aw.view.y - (/*size.y / 6*/ defaultBubbleSize / 4))
            }
        }

        if (objRight.x + objRight.width < size.x - (size.x / 4) && !isInertialMoveFromRight) {
            isBreakMoving = true
            isInertialMoveFromRight = true

            if (objLeftTop.y > 0)
                isInertialMoveFromTop = true
            if ((objBottom.y + objBottom.height) < size.y)
                isInertialMoveFromBottom = true

            if (isInertialMoveFromTop) {
                val moveY = objLeftTop.y
                for (aw in animList) {
                    aw.setY(aw.view.y - moveY)
                }
            }
            if (isInertialMoveFromBottom) {
                val moveY = objBottom.height
                for (aw in animList) {
                    aw.setY(aw.view.y + moveY)
                }
            }

            for (aw in animList) {
                aw.setX(aw.view.x + (size.x - aw.view.width))
            }
        }

        if (objBottom.y + objRight.height < size.y - (size.y / 4) && !isInertialMoveFromBottom) {
            isBreakMoving = true
            isInertialMoveFromBottom = true

            if (objLeftTop.x > 0)
                isInertialMoveFromLeft = true
            if ((objRight.x + objRight.width) < size.x)
                isInertialMoveFromRight = true

            if (objLeftTop.x > 0) {
                val moveX = objLeftTop.x
                for (aw in animList) {
                    aw.setX(aw.view.x - moveX)
                }
            }

            if (objRight.x + objRight.width < size.x) {
                val moveX = size.x - (objRight.width * 1.5).roundToInt()
                for (aw in animList) {
                    aw.setX(aw.view.x + moveX)
                }
            }

            for (aw in animList) {
                aw.setY(aw.view.y + objBottom.width)
            }
        }

        if(isInertialMoveFromLeft || isInertialMoveFromRight ||
         isInertialMoveFromTop || isInertialMoveFromBottom) {
            for (a in animList)
                a.inertialMove(
                    isX = isInertialMoveFromLeft || isInertialMoveFromRight,
                    isY = isInertialMoveFromTop || isInertialMoveFromBottom
                )
            isInertialMove = false
            isSlowed = false
        }

        isInertialMoveFromLeft = false
        isInertialMoveFromTop = false
        isInertialMoveFromRight = false
        isInertialMoveFromBottom = false

        if (isBreakMoving) {
            isInertialMove = false
            isSlowed = false
            val handler = Handler()
            handler.postDelayed({
                scale(++animateId)
            }, 250)
            handler.postDelayed({
                isBreakMoving = false
            }, 750)
        }
    }

    private fun checkIsUser(e: MotionEvent) {
        val x = e.x.toDouble()
        val y = e.y.toDouble()
        for (i in 0 until grid.childCount) {
            val paramsBubble =
                grid.getChildAt(i).layoutParams as AbsoluteLayout.LayoutParams
            val bX = paramsBubble.x.toDouble()
            val bY = paramsBubble.y.toDouble()

            if(x > bX && x < bX + paramsBubble.width && y > bY && y < bY + paramsBubble.height) {
                val fm = activity!!.supportFragmentManager
                isOpenedUser = true
                fm.beginTransaction().replace(R.id.container, UserPreviewFragment(
                    users[i], fm)).addToBackStack(null).commit()
                return
            }
        }
    }

    /**
     * create View.OnTouchListener for grid
     * @return the View.OnTouchListener
     */
    private fun getTouchListener(): View.OnTouchListener {
        return View.OnTouchListener { _, e ->

            loop@ for (i in 0 until grid.childCount) {
                val c = grid.getChildAt(i)
                val p = points[i]
                when (e.action) {
                    0 -> {
                        animList[i].params()
                        try {
                            p.x = (c.x - e.rawX).roundToInt()
                            p.y = (c.y - e.rawY).roundToInt()
                        } catch(ignored: IllegalArgumentException) {

                        }
                        startClickMillis = Calendar.getInstance().timeInMillis
                    }
                    1 -> {
//                        if(Calendar.getInstance().timeInMillis - startClickMillis < 100) {
//                            if(!isOpenedUser)
//                                checkIsUser(e)
//                        }
                        if (Calendar.getInstance().timeInMillis - lastMoveMillis < 20) {
                            prevPoint.set(e.x.roundToInt(), e.y.roundToInt())
                            endClickMillis = Calendar.getInstance().timeInMillis
                            afterClickScale()

                            inertialX /= 2
                            inertialY /= 2
                            if(abs(inertialX) > 2 && abs(inertialY) > 2) {
                                if(inertialX > 40)
                                    inertialX = 40
                                else if(inertialX in 0..14)
                                    inertialX = 15

                                if(inertialY > 40)
                                    inertialY = 40
                                else if(inertialY in 0..14)
                                    inertialY = 15

                                if(inertialX < -40)
                                    inertialX = -40
                                else if(inertialX in -0 downTo -14)
                                    inertialX = -15

                                if(inertialY < -40)
                                    inertialY = -40
                                else if(inertialY in -0 downTo -14)
                                    inertialY = -15
                            }

                            isInertialMove = true
                            //inertia()
                            return@OnTouchListener true
                        }
                    }
                    2 -> {
                        if (isBreakMoving)
                            return@OnTouchListener false
                        inertialX = (e.x - prevPoint.x.toFloat()).roundToInt()
                        inertialY = (e.y - prevPoint.y.toFloat()).roundToInt()

                        lastMoveMillis = Calendar.getInstance().timeInMillis

                        validatePositionAtBorder()

                        if (isBreakMoving)
                            break@loop

                        val anim = animList[i]
                        anim.setCoordinates(e.rawX + p.x, e.rawY + p.y)
                        anim.scale(getScaleSize(c))
                        anim.startAnimation()
                    }
                }
            }
            true
        }
    }

    /**
     * calculated and return the multiplier for scale view size
     * @param o the view
     * @return float value at range 0.0f -> 0.8f
     */
    private fun getScaleSize(o: View): Float {
        var s =
            Point((o.x + o.width / 2).toInt(), (o.y + o.height / 2).toInt() + defaultBubbleSize / 2).distance(center) / 1000
        when {
            s > maxScaleMultiplier -> s = maxScaleMultiplier
            s < minScaleMultiplier -> s = minScaleMultiplier
            else -> s += sqrt(s).pow(3)
        }
        s = 1 - s
        if(s < minScaleMultiplier)
            s = minScaleMultiplier
        return s
    }

    /**
     * loop about all child of the grid
     * scale child`s size with check if view show at the screen
     * @param _id id of the current animation cycle
     */
    private fun scale(_id: Long) {
        if (animateId == _id) {
            for (i in 0 until grid.childCount) {
                if (animateId != _id)
                    return
                val c = grid.getChildAt(i)
                if (isViewShowOnScreen(c)) {
                    val anim = animList[i]
                    val startScale = getScaleSize(c)

                    (c as TextView).text = startScale.toString()
                    anim.scale(startScale)
                    anim.startScaleAnimation()
                }
            }
        }
    }

    /**
     * loop about all child of the grid
     * scale child`s size without check if view show at the screen
     */
    private fun scale() {
        for (i in 0 until grid.childCount) {
            if (animateId != 0L)
                return
            val c = grid.getChildAt(i)
            val anim = animList[i]
            val startScale = getScaleSize(c)
            (c as TextView).text = startScale.toString()
            anim.scale(startScale)
            anim.startScaleAnimation()
        }
    }

    /**
     * return true if view shows at screen
     * @param v the view
     * @return the result
     */
    private fun isViewShowOnScreen(v: View): Boolean {
        val x = v.x
        val y = v.y
        return x > 0 - defaultBubbleSize && x < size.x + defaultBubbleSize && y > 0 - defaultBubbleSize && y < size.y + defaultBubbleSize
    }

    /**
     * called when user up finger from screen
     * post runnable obj
     * this obj will scale all grid child`s
     */
    private fun afterClickScale() {
        val id = ++animateId
        animateId = id
        val myHandler = Handler()
        myHandler.postDelayed(object : Runnable {
            override fun run() {
                if (Calendar.getInstance().timeInMillis - endClickMillis < 5000) {
                    scale(id)
                    validatePositionAtBorder()
                    myHandler.postDelayed(this, 10)
                }
            }
        }, 10)

    }

    @SuppressLint("ClickableViewAccessibility", "InflateParams")
    private fun init() {
        rows = sqrt(users.size.toDouble()).toInt()

        for (i in 0 until users.size)
            points.add(Point())

        grid.setOnTouchListener(getTouchListener())
        users.shuffle()

        val m: Int = defaultBubbleSize / 10

        defSizeX = ((size.x / 2.45)).toInt()
        defSizeY = (size.y / 4.9).toInt()
        sizeX = defSizeX * (rows - 1)
        sizeY = defSizeY * (rows - 1)
        startX = 0 - (sizeX / 2)
        startY = 0 - (sizeY / 2)

        for (i2 in 0 until rows) {
            for (j in 0 until rows) {
                if (activity == null)
                    return

                val view = activity!!.layoutInflater.inflate(
                    R.layout.item_bubble,
                    null,
                    false
                )
                val bubble = view.findViewById<TextView>(R.id.bubble_img)

                val x = startX + defSizeX * j
                val y = startY + defSizeY * i2

                val params = AbsoluteLayout.LayoutParams(
                    this@BubbleFragment.defaultBubbleSize,
                    this@BubbleFragment.defaultBubbleSize,
                    x,
                    y
                )

                if (i2 % 2 != 0) {
                    val i5 = params.x
                    val d4 = size.x.toDouble()
                    params.x = i5 + (d4 / 2.45).toInt() / 2
                }

                animList.add(AnimateWrapper(bubble))
                bubble.layoutParams = params

                grid.addView(view)

            }
        }

        // -> find the left, top, right and bottom elements
        objLeftTop = grid.getChildAt(0)
        objRight = if (rows % 2 == 0)
            grid.getChildAt((rows / 2) + (rows - 1))
        else {
            if (rows > 2)
                grid.getChildAt((rows * 2) - 1)
            else
                grid.getChildAt(rows - 1)
        }
        objBottom = grid.getChildAt(grid.childCount - 1)

        for (c in 0 until grid.childCount) {
            val bubble = grid.getChildAt(c) as CircleImageView
            Picasso.with(context!!).load("${Values.imgUrl}/${users[c].avatarSmall}").into(bubble)

            /*bubble.setOnClickListener {
                Log.d("distance to center",
                    Math.pixelsToDp(
                        Point(
                            (bubble.x + bubble.width / 2).roundToInt(),
                            (bubble.y + bubble.height / 2).roundToInt()
                        )
                            .distance(center),
                        context!!)
                        .toString())
                animList[c].scale(1.5f)
                animList[c].startScaleAnimation()
            }
            bubble.setOnClickListener {
                val fm = fragmentManager!!
                val t = fm.beginTransaction()
                val fragment = UserPreviewFragment(users[c], fm)
                t.replace(R.id.container, fragment)
                t.addToBackStack(null)
                t.commit()
            }*/
        }
    }

    /**
     * fetch the users data from the server
     * create all child`s view and set start params
     */
    private fun fetch() {
        if(users.isEmpty())
            disposable.addAll(
                api.getUsers()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Response<List<UserData>>>() {
                        override fun onSuccess(r: Response<List<UserData>>) {
                            users.addAll(r.body() ?: listOf())
                            init()
                            scale()
                        }
                        override fun onError(e: Throwable) {
                        }

                    })
            )
        else init()
    }
}