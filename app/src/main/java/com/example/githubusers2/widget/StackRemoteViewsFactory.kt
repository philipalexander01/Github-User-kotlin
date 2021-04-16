package com.example.githubusers2.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import android.widget.Toast
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubusers2.R
import com.example.githubusers2.db.DatabaseContract
import com.example.githubusers2.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutionException


internal class StackRemoteViewsFactory(val context: Context) : RemoteViewsService.RemoteViewsFactory {
    private val mWidgetItems = ArrayList<String>()
    override fun onCreate() {

        GlobalScope.launch(Dispatchers.Main) {
            val deferred = async(Dispatchers.IO) {
                val cursor = context.contentResolver.query(
                    DatabaseContract.GithubColumns.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
                )
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favData = deferred.await()
            if (favData.size > 0) {
                for(i in 0 until favData.size){
                    mWidgetItems.add(favData[i].avatar_url.toString())
                }

            }
        }

    }

    override fun onDataSetChanged() {


    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)
        var bmp: Bitmap? = null
        try {
            bmp = Glide.with(context)
                .asBitmap()
                .load(mWidgetItems[position])
                .apply(RequestOptions().fitCenter()).placeholder(R.drawable.ic_launcher_background)
                .submit(512,512)
                .get()
            rv.setImageViewBitmap(R.id.imageView, bmp)

        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
        Toast.makeText(context, "${bmp}", Toast.LENGTH_SHORT).show()

        val extras = bundleOf(
                FavoriteWidget.EXTRA_ITEM to position
            )
            val fillInIntent = Intent()
            fillInIntent.putExtras(extras)

            rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)

        return rv
    }
    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false


}