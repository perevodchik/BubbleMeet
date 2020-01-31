package com.perevodchik.bubblemeet.ui.mainmenu.bubble

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AbsoluteLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.custom.Bubble
import com.perevodchik.bubblemeet.data.model.UserData
import com.perevodchik.bubblemeet.ui.filter.FilterActivity
import com.perevodchik.bubblemeet.ui.user.UserPreviewFragment
import com.perevodchik.bubblemeet.util.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.*
import kotlin.math.*

class BubbleFragment : Fragment() {
    private lateinit var grid: AbsoluteLayout
    private val api: Api = Api()
    private val disposable = CompositeDisposable()
    private val presenter = BubblePresenter(this)
    /** count rows at the grid */
    private var rows: Int = 0
    /** container for hold size screen */
    private var size: Point = Point()
    private val points: MutableList<Point> = mutableListOf()
    /** list with helper class for animation */
    //private val animList: MutableList<AnimateWrapper> = mutableListOf()
    /** display */
    private val display by lazy { activity!!.windowManager.defaultDisplay }
    /** default (start and max) bubble size */
    //private val defaultBubbleSize by lazy { Math.dpToPixel(200.0f, context!!).roundToInt() }
    private var defaultBubbleSize: Int = 0
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
    private var sizeX: Int = 0
    private var sizeY: Int = 0
    private var startX: Int = 0
    private var startY: Int = 0

    private var isInertialMove = false
    private var isSlowed = false
    private var inertialX: Int = -5
    private var inertialY: Int = -5

    private var isFirstLoad = true
    private var bubbleDelimeter: Float = 0.0f
    private var distanceDelimeter: Int = 1000

    companion object {
        private const val minScaleMultiplier: Float = 0.025f
        private const val maxScaleMultiplier: Float = 0.8f
        private const val borderMultiplier = 5
        private const val inertiaScale = 1
        private const val borderMinus = -15
        private const val borderPlus= 15
        private val users: MutableList<UserData> = mutableListOf()
        private var bubbles: MutableList<Bubble> = mutableListOf()

        fun newInstance() = BubbleFragment()
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_bubble_grid0, null)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        grid = activity!!.findViewById(R.id.grid_bubbles)
        grid.setOnTouchListener(getTouchListener())
        display.getSize(size)
        Log.d("screen size => ", "$size")

        bubbleDelimeter = when(Math.pixelsToDp(size.x + 0f, context!!).roundToInt()) {
            in 100..200 -> 10.5f
            in 200..300 -> 8.5f
            in 300..400 -> 5.25f
            in 400..500 -> 5.5f
            in 500..550 -> 5.0f
            in 550..600 -> 4.5f
            in 600..700 -> 4.0f
            in 700..800 -> 3.5f
            else -> 2.75f
        }
        distanceDelimeter = when(Math.pixelsToDp(size.x + 0f, context!!).roundToInt()) {
            in 100..200 -> 450
            in 200..300 -> 500
            in 300..400 -> 600
            in 400..800 -> 1000
//            in 500..550 -> 900
//            in 550..600 -> 800
//            in 600..700 -> 700
//            in 700..800 -> 600
            else -> 1000
        }

        Log.d("bubbleDelimeter =>", "$bubbleDelimeter")
        Log.d("distanceDelimeter =>", "$distanceDelimeter")

        defaultBubbleSize = (Math.dpToPixel(
            size.x / bubbleDelimeter,
            context!!
        )).roundToInt()

        fetch()
        toggle()
    }

    override fun onStart() {
        super.onStart()
        toggle()
        animateId = 0L
        isOpenedUser = false
        val list = mutableListOf<UserData>()
        list.addAll(UserInstance.allUsers)

        if (UserInstance.filters.size == 0 && list.size == 0)
            return

        presenter.filterByGender(list, UserInstance.filters["gender"] ?: "null")
        presenter.filterByAge(list, UserInstance.filters["age"] ?: "null")
        presenter.filterByLocation(list, UserInstance.filters["location"] ?: "null")
        presenter.filterByEyeColor(list, UserInstance.filters["eye"] ?: "null")
        presenter.filterByHeight(list, UserInstance.filters["height"] ?: "null")
        presenter.filterBySmoking(list, UserInstance.filters["smoking"] ?: "null")
        presenter.filterByMarried(list, UserInstance.filters["married"] ?: "null")
        presenter.filterByChildren(list, UserInstance.filters["children"] ?: "null")
        presenter.filterByLookingFor(list, UserInstance.filters["looking"] ?: "null")
        presenter.filterByLoveToCook(list, UserInstance.filters["cook"] ?: "null")

        users.clear()
        users.addAll(list)
        grid.removeAllViewsInLayout()
        init()
    }

    private fun toggle() {
        print("main on start")
        val bToggle = activity?.findViewById<ImageView>(R.id.toggle)
        bToggle?.setImageDrawable(resources.getDrawable(R.drawable.filter_btn, null))
        bToggle?.visibility = View.VISIBLE
        bToggle?.setOnClickListener {
            activity?.startActivityForResult(Intent(activity, FilterActivity::class.java), 33)
        }
    }

    /**
     * check is bubble`s out of the left border
     */
    private fun checkLeftBorder(isBig: Boolean): Boolean {

        if (((objLeftTop.x + objLeftTop.width > defaultBubbleSize / borderMultiplier && !isBig) || (objLeftTop.x > size.x && isBig)) && !isInertialMoveFromLeft) {
//            isBreakMoving = true
//            isInertialMove = false
            isInertialMoveFromLeft = true

            checkTopBorder(isBig)
            checkBottomBorder(isBig)

            inertialX = borderMinus

//            for (b in bubbles) {
//                b.animateWrapper.params(100.0f)
//                b.animateWrapper.setX(b.view.x - objLeftTop.x - objLeftTop.width)
//            }
        }
        return isInertialMoveFromLeft
    }

    /**
     * check is bubble`s out of the top border
     */
    private fun checkTopBorder(isBig: Boolean): Boolean {
        if (((objLeftTop.y > defaultBubbleSize / borderMultiplier && !isBig) || (objRight.y > size.y / 2 && isBig)) && !isInertialMoveFromTop) {
//            isBreakMoving = true
//            isInertialMove = false
            isInertialMoveFromTop = true

            checkLeftBorder(isBig)
            checkRightBorder(isBig)

            if(inertialY > 0)
                inertialY = borderMinus

//            for (b in bubbles) {
//                b.animateWrapper.params(100.0f)
//                b.animateWrapper.setY(b.view.y - objLeftTop.y - objLeftTop.height)
//            }
        }
        return isInertialMoveFromTop
    }


    /**
     * check is bubble`s out of the right border
     */
    private fun checkRightBorder(isBig: Boolean): Boolean {
        if (((objRight.x < size.x - defaultBubbleSize / borderMultiplier && !isBig) || (objRight.x < size.x / 2 && isBig)) && !isInertialMoveFromRight) {
//            isBreakMoving = true
//            isInertialMove = false
            isInertialMoveFromRight = true

            checkTopBorder(isBig)
            checkBottomBorder(isBig)

            inertialX = borderPlus

//            for (b in bubbles) {
//                b.animateWrapper.params(100.0f)
//                b.animateWrapper.setX(b.view.x + size.x - objRight.x)
//            }
        }
        return isInertialMoveFromRight
    }


    /**
     * check is bubble`s out of the bottom border
     */
    private fun checkBottomBorder(isBig: Boolean): Boolean {
        if (((objBottom.y < size.y - (size.y / borderMultiplier) && !isBig) || (objBottom.y < size.y / 2 && isBig)) && !isInertialMoveFromBottom) {
//            isBreakMoving = true
//            isInertialMove = false
            isInertialMoveFromBottom = true

            checkLeftBorder(isBig)
            checkRightBorder(isBig)

            if(inertialY < 0)
                inertialY = borderPlus

//            for (b in bubbles) {
//                b.animateWrapper.params(100.0f)
//                b.animateWrapper.setY(b.view.y + size.y - objBottom.y - objBottom.height)
//            }
        }
        return isInertialMoveFromBottom
    }

    private fun moveFromBorder() {
        for (b in bubbles)
            b.animateWrapper.inertialMove(
                isX = isInertialMoveFromLeft || isInertialMoveFromRight,
                isY = isInertialMoveFromTop || isInertialMoveFromBottom
            )

//        isInertialMove = false
//        isSlowed = false
        isInertialMoveFromLeft = false
        isInertialMoveFromTop = false
        isInertialMoveFromRight = false
        isInertialMoveFromBottom = false
        var counter = 10
        val handler = Handler()

        handler.postDelayed(object : Runnable {
            override fun run() {
                scale(++animateId)
                if (--counter > 0)
                    handler.postDelayed(this, 50)
            }
        }, 50)
        handler.postDelayed({
            isBreakMoving = false
        }, 250)
    }

    /**
     * check if grid out of border
     * if need start "back" animation
     * and blocking user permission to move grid with finger for 500 millis
     */
    private fun validatePositionAtBorder(isBig: Boolean) {
        if(bubbles.size < 30)
            return
        if (checkLeftBorder(isBig) || checkTopBorder(isBig) ||
            checkRightBorder(isBig) || checkBottomBorder(isBig)
        ) moveFromBorder()

    }

    private fun checkIsUser(e: MotionEvent) {
        val x = e.x.toDouble()
        val y = e.y.toDouble()
        for (b in bubbles) {
            val d = b.view.x.toDouble()
            val d2 = defaultBubbleSize.toDouble()
            val d3 = 1.0 - getScaleSize(b)
            val d4 = d2 * d3 / 2.0
            if (x > d + d4 && y > b.view.y) {
                val d5 = (b.view.x + b.view.height).toDouble() - 100
                val d6 = defaultBubbleSize.toDouble()
                val d7 = b.scaleSize
                val d8 = d6 * d7 / 2.0
                if (x < d5 + d8 && e.y < (b.view.y + b.view.height)) {
                    animateId = 0L
                    isOpenedUser = true
                    val fm = fragmentManager
                    fm?.beginTransaction()
                        ?.replace(R.id.container, UserPreviewFragment(b.userData, fm))
                        ?.addToBackStack(null)
                        ?.commit()
                    for (bubble in bubbles)
                        bubble.animateWrapper.stopAnimation()
                    return
                }
            }
        }
    }

    private fun startInertial() {
        inertialX /= 2
        inertialY /= 2
        if (abs(inertialX) > 2 && abs(inertialY) > 2) {
            if (inertialX > 60)
                inertialX = 60
            else if (inertialX in 0..20)
                inertialX = 20

            if (inertialY > 60)
                inertialY = 60
            else if (inertialY in 0..20)
                inertialY = 20

            if (inertialX < -60)
                inertialX = -60
            else if (inertialX in -0 downTo -20)
                inertialX = -20

            if (inertialY < -60)
                inertialY = -60
            else if (inertialY in -0 downTo -20)
                inertialY = -20
        }

        isInertialMove = true
        validatePositionAtBorder(false)
        if (isInertialMove)
            inertial()
    }

    /**
     * create View.OnTouchListener for grid
     * @return the View.OnTouchListener
     */
    private fun getTouchListener(): View.OnTouchListener {
        return View.OnTouchListener { _, e ->

            loop@ for (i in 0 until bubbles.size) {
                val b = bubbles[i]
                val p = points[i]
                when (e.action) {
                    0 -> {
                        isInertialMove = false
                        prevPoint.set(e.x.roundToInt(), e.y.roundToInt())
                        if (isBreakMoving)
                            return@OnTouchListener false
                        bubbles[i].animateWrapper.params()
                        try {
                            p.x = (b.view.x - e.rawX).roundToInt()
                            p.y = (b.view.y - e.rawY).roundToInt()
                        } catch (ignored: IllegalArgumentException) {

                        }
                        startClickMillis = Calendar.getInstance().timeInMillis
                    }
                    1 -> {
                        if (Calendar.getInstance().timeInMillis - startClickMillis < 75) {
                            if (!isOpenedUser)
                                checkIsUser(e)
                        }
                        if (Calendar.getInstance().timeInMillis - lastMoveMillis < 20) {
                            startInertial()
                        }
                        endClickMillis = Calendar.getInstance().timeInMillis
                        return@OnTouchListener true
                    }
                    2 -> {
                        if (isBreakMoving)
                            return@OnTouchListener false
                        inertialX = (e.x - prevPoint.x.toFloat()).roundToInt()
                        inertialY = (e.y - prevPoint.y.toFloat()).roundToInt()

                        lastMoveMillis = Calendar.getInstance().timeInMillis

                        val anim = bubbles[i].animateWrapper
                        anim.setCoordinates(e.rawX + p.x, e.rawY + p.y)
                        anim.scale(getScaleSize(b))
                        anim.startAnimation()
                    }
                }
            }
            true
        }
    }

    private fun inertial() {
        val myHandler = Handler()
        myHandler.postDelayed(object : Runnable {
            override fun run() {
                if (isInertialMove) {
                    if (inertialX == 0 && inertialY == 0) {
                        isInertialMove = false
                    }
                    if (inertialX < 0 && inertialY > 0) {
                        if (isSlowed) {
                            inertialX += inertiaScale
                            inertialY -= inertiaScale
                            isSlowed = false
                        } else {
                            isSlowed = true
                        }
                    }
                    if (inertialX < 0 && inertialY < 0) {
                        if (isSlowed) {
                            inertialX += inertiaScale
                            inertialY += inertiaScale
                            isSlowed = false
                        } else {
                            isSlowed = true
                        }
                    }
                    if (inertialX > 0 && inertialY > 0) {
                        if (isSlowed) {
                            inertialX -= inertiaScale
                            inertialY -= inertiaScale
                            isSlowed = false
                        } else {
                            isSlowed = true
                        }
                    }
                    if (inertialX > 0 && inertialY < 0) {
                        if (isSlowed) {
                            inertialX -= inertiaScale
                            inertialY += inertiaScale
                            isSlowed = false
                        } else {
                            isSlowed = true
                        }
                    }
                    if (inertialX > 0 && inertialY == 0) {
                        if (isSlowed) {
                            inertialX -= inertiaScale
                            isSlowed = false
                        } else {
                            isSlowed = true
                        }
                    }
                    if (inertialX < 0 && inertialY == 0) {
                        if (isSlowed) {
                            inertialX += inertiaScale
                            isSlowed = false
                        } else {
                            isSlowed = true
                        }
                    }
                    if (inertialY > 0 && inertialX == 0) {
                        if (isSlowed) {
                            inertialY -= inertiaScale
                            isSlowed = false
                        } else {
                            isSlowed = true
                        }
                    }
                    if (inertialY < 0 && inertialX == 0) {
                        if (isSlowed) {
                            inertialY += inertiaScale
                            isSlowed = false
                        } else {
                            isSlowed = true
                        }
                    }
                    move()
                    scale(++animateId)
                    myHandler.postDelayed(this, 25)
                }
            }
        }, 25)
    }

    private fun move() {
        for (b in bubbles) {
            if (isInertialMove)
                b.animateWrapper.inertial(inertialX + 0.0f, inertialY + 0.0f)
        }
        validatePositionAtBorder(false)
    }

    /**
     * calculated and return the multiplier for scale view size
     * @param o the view
     * @return float value
     */
    private fun getScaleSize(o: Bubble): Float {
        var s =
            Point(
                (o.view.x + o.view.width / 2).toInt(),
                (o.view.y + o.view.height / 2).toInt() + 100
            ).distance(center) / distanceDelimeter

//        (o.view as TextView).text = s.toString()
        if(s > 0.525) {
            s = minScaleMultiplier
            o.scaleSize = s
            return s
        }

        when {
            s > maxScaleMultiplier -> s = maxScaleMultiplier
            s < minScaleMultiplier -> s = minScaleMultiplier
            else -> s += sqrt(s).pow(if(bubbleDelimeter > 5.0) 4 else 5)
        }

        s = 1 - s
        if (s < minScaleMultiplier)
            s = minScaleMultiplier

        s = exp(s) - 0.85f

        if(s < 0.27f) {
            o.scaleSize = s
            return s
        }

        when {
            s > maxScaleMultiplier -> s = maxScaleMultiplier
            s < minScaleMultiplier -> s = minScaleMultiplier
        }
        if(s > maxScaleMultiplier) s = maxScaleMultiplier

        o.scaleSize = s
//        Log.d("distance3 => ", "${o.scaleSize}")
        return s
    }

    /**
     * loop about all child of the grid
     * scale child`s size with check if view show at the screen
     * @param _id id of the current animation cycle
     */
    private fun scale(_id: Long) {
        if (animateId == _id) {
            for (i in 0 until bubbles.size) {
                if (animateId != _id)
                    return
                val c = bubbles[i]
                //if (isViewShowOnScreen(c.view)) {
                    val startScale = getScaleSize(c)

                    c.scaleSize = startScale
                    c.animateWrapper.scale(startScale)
                    c.animateWrapper.startScaleAnimation()
//                    (c.view as TextView).text = startScale.toString()
                //}
            }
        }
    }

    /**
     * loop about all child of the grid
     * scale child`s size without check if view show at the screen
     */
    private fun scale() {
        for (b in bubbles) {
            val startScale = getScaleSize(b)
            b.scaleSize = startScale
            b.animateWrapper.scale(startScale)
            b.animateWrapper.startScaleAnimation()
//            (b.view as TextView).text = startScale.toString()
        }
    }

    @SuppressLint("ClickableViewAccessibility", "InflateParams", "SetTextI18n")
    private fun init() {
        if (isFirstLoad) {
            users.shuffle()
            isFirstLoad = false
        }
        bubbles.clear()
        val iter = users.iterator()
        rows = sqrt(users.size.toDouble()).toInt()

        for (i in 0 until users.size)
            points.add(Point())

        val diff = 0 //if (size.x > 1000) 50 else 0
        // прорахування скільки пікселів потрібно для бульбашки
        defSizeX = ((size.x / 2.45)).toInt() - diff
        defSizeY = (size.y / 4.9).toInt() - 50
        // прорахування загальну величину "холсту"
        sizeX = defSizeX * (rows - 1)
        sizeY = defSizeY * (rows - 1)
        // прорахування стартової позиції де будемо розміщувати бульбашки
        startX = 0 - (sizeX / 2)
        startY = 0 - (sizeY / 2)

        for (i2 in 0 until rows) {
            for (j in 0 until rows) {
                if (activity == null)
                    return
                if (!iter.hasNext())
                    return

                val view = activity!!.layoutInflater.inflate(
                    R.layout.item_bubble,
                    null,
                    false
                )
                val ud = iter.next()
                val bubble = Bubble(ud, view)
                val x = startX + defSizeX * j
                val y = startY + defSizeY * i2
                bubble.view.x = x + 0.0f
                bubble.view.y = y + 0.0f
                val params = AbsoluteLayout.LayoutParams(
                    this@BubbleFragment.defaultBubbleSize,
                    this@BubbleFragment.defaultBubbleSize,
                    0,
                    0
                )

                if (i2 % 2 != 0) {
                    bubble.view.x = bubble.view.x - (defSizeX / 2.0f)
                }

                bubble.view.layoutParams = params
//                (bubble.view as TextView).text = "${bubble.userData.name} -> ${counter++}"

                //scale(bubble.view)
                grid.addView(bubble.view)
                bubbles.add(bubble)

//                bubble.view.setOnClickListener { getScaleSize(bubble) }
                bubble.animateWrapper.startAlphaAnimation()
            }
        }

        if (grid.childCount == 0)
            return
        // -> find the left, top, right and bottom elements
        objLeftTop = grid.getChildAt(0)

        objRight = if (rows > 2) {
            grid.getChildAt(rows * 2 - 1)
        } else {
            grid.getChildAt(rows - 1)
        }
        objBottom = grid.getChildAt(grid.childCount - 1)

        // load avatarSmall into bubbles
        for (cc in 0 until grid.childCount) {
            val bubble = grid.getChildAt(cc)
            Glide.with(context!!).load("${Values.imgUrl}/${users[cc].avatarSmall}").into(bubble as CircleImageView)
            Log.d("load img $cc", "${Values.imgUrl}/${users[cc].avatarSmall}")
//            Picasso.with(context!!).load("${Values.imgUrl}/${users[cc].avatarSmall}")
//                .into(bubble as CircleImageView)
        }
        scale()
        isInertialMove = true
        if(bubbleDelimeter > 5.25)
            startInertial()
        else move()
        scale(++animateId)
    }

    /**
     * fetch the users data from the server
     * create all child`s view and set start params
     */
    private fun fetch() {
        if (users.isEmpty())
            if (UserInstance.allUsers.isEmpty()) {
                disposable.addAll(
                    api.getUsers()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object :
                            DisposableSingleObserver<Response<List<UserData>>>() {
                            override fun onSuccess(r: Response<List<UserData>>) {
                                UserInstance.addUsers(r.body() ?: listOf())
                                users.addAll(UserInstance.allUsers)
                                init()
                            }

                            override fun onError(e: Throwable) {
                            }

                        })
                )
            } else {
                users.addAll(UserInstance.allUsers)
                users.shuffle()
                init()
            }
        else {
            init()
        }
    }
}