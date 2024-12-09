package com.UsreLaborDispatch1.www.UsreLaborDispatch1

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.UsreLaborDispatch1.www.UsreLaborDispatch1.pdf.PdfViewer
import com.UsreLaborDispatch1.www.sync.adapter.FilesAdapter
import com.UsreLaborDispatch1.www.sync.helper.getLocalPdfFile
import com.UsreLaborDispatch1.www.sync.helper.showMsg
import com.UsreLaborDispatch1.www.sync.model.FileInfo
import com.UsreLaborDispatch1.www.sync.viewmodel.FilesViewModel
import com.UsreLaborDispatch1.www.sync.viewmodel.JobViewModel
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class DownloadsActivity : AppCompatActivity() {

    var rc: RecyclerView?=null
    var llEmpty: View? = null
    var jobViewModel:JobViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_downloads)
        rc = findViewById(R.id.rc)
        llEmpty = findViewById(R.id.llEmpty)

        setUpRecyclerView()
        jobViewModel = ViewModelProvider(this).get(JobViewModel::class.java)
        //requestStoragePersmission()
        initFilesUpdate()

    }




    private fun initFilesUpdate() {
        jobViewModel?.getPdfFiles(this)?.observe(this, Observer {
            isEmptyList(it)
            it?.apply {
                adapter?.submitList(this)
            }
        })
    }

    private fun isEmptyList(it: List<FileInfo>?) {
        if (it.isNullOrEmpty())
           llEmpty?.visibility = View.VISIBLE
        else
           llEmpty?.visibility = View.GONE
    }


    var adapter: FilesAdapter? = null
    private fun setUpRecyclerView() {


        adapter = FilesAdapter()

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
       rc?.setLayoutManager(layoutManager)
       rc?.setHasFixedSize(true)
       rc?.requestDisallowInterceptTouchEvent(true)
       rc?.setAdapter(adapter)

        adapter?.setOnItemClickListener(object: FilesAdapter.OnItemClickListener {
            override fun OnItemClicked(file: FileInfo?) {
                file?.apply {
                  displayPdf(getFileAdress(),getFileName())
                }

            }
        })


//        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
//                0,
//                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
//        ) {
//            override fun onMove(
//                    recyclerView: RecyclerView,
//                    viewHolder: RecyclerView.ViewHolder,
//                    target: RecyclerView.ViewHolder
//            ): Boolean {
//                return false
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                adapter?.getItemName(viewHolder.adapterPosition)?.apply {
//                 filesViewModel?.deletePdfItem(this)
//                }
//            }
//        }).attachToRecyclerView(rc)




    }

    private fun displayPdf(fileUrl:String,fileName:String) {
        PdfViewer.start(this,fileUrl,fileName)
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, DownloadsActivity::class.java))
        }
    }
}