package com.ose4g.currencyconverter

import android.app.Activity
import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var pageTransformer1:ViewPager2.PageTransformer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list:ArrayList<Fragment> = ArrayList()
        list.add(HomeFragment())
        list.add(GraphFragment())
        val viewModel:ConverterViewModel = ViewModelProvider(this).get(ConverterViewModel::class.java)
        viewModel.viewPager2 = viewpager;
        viewpager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle, list)
        viewpager.clipToPadding = false


        //pagetransformer or the viewpager
         pageTransformer1 = ViewPager2.PageTransformer { page, position ->

            val page_height = page.height//height of the page

            if(position<-1)//for the first page when completely out o view
                page.alpha=0f
            else if(position<=0)//when first page still in view
            {
                //make sure that some of the page remains in view by translating it downward
                if(position<=-0.9)
                {
                    val h = 0.2f*page_height
                    //translate the page so that at least 0.1 of the height is showing
                    page.translationY = -h*(10*position+9)
                }

                else//for page position with view greater than 0.1 of the screen
                {
                    if(position==0f )
                    {
                        //show full screen for only the first page
                        if(viewpager.currentItem==0)
                            page.translationY = 0f
                    }
                    else//dont do any translation
                        page.translationY = 0f
                }

            }

            //from here is for the second page
            else if(position<0.1)
            {
                //if the position is less then 0.1 of the screen height
                //then keep is constant
                var translation = (0.1f-position)*page_height
                page.translationY = (translation)
            }

            else if(position >0.9)
            {
                //make sure that part of the second screen shows
                var margin = 0.1f*page_height
                var translation = (1-position)*page_height
                page.translationY = -1*(margin-translation)

            }
            else if(position<0.9)
            {
                page.translationY = 0f
            }
        }

        //pagetransformer only gets triggered when view is shifted.
        //this block of code shifts the view

        viewpager.setPageTransformer(pageTransformer1)
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            viewpager.beginFakeDrag()
            viewpager.fakeDragBy(0.2f)
            Log.i("screen",0.1f.toString())
//            Log.i("screen",""+getScreenHeight(this))
//            Log.i("screen_adj",""+(0.001f*getScreenHeight(this)))
            viewpager.endFakeDrag()
        }, 1)
    }

    fun getScreenHeight(activity: Activity): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = activity.windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.height() - insets.top - insets.bottom
        } else {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.heightPixels
        }
    }
}