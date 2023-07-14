package com.typ.muslim.ui.prays

import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textview.MaterialTextView
import com.typ.muslim.R
import com.typ.muslim.features.prays.models.Pray
import com.typ.muslim.utils.stringRes

class PrayViewerBottomSheet(ctx: Context, pray: Pray) {

    private val bs: BottomSheetDialog = BottomSheetDialog(ctx)

    init {
        bs.apply {
            setContentView(R.layout.bs_pray_view)
            findViewById<MaterialTextView>(R.id.tv_pray_name)!!.text = stringRes(ctx, pray.prayNameRes)
            findViewById<MaterialTextView>(R.id.tv_pray_time)!!.text = pray.getFormattedTime(ctx)
            findViewById<MaterialTextView>(R.id.tv_pray_definition)!!.text = stringRes(ctx, pray.prayDefRes)
        }
    }

    fun show() {
        bs.show()
    }

}
