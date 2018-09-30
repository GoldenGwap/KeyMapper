package io.github.sds100.keymapper.ActionTypeFragments

import android.app.Activity
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.sds100.keymapper.Action
import io.github.sds100.keymapper.ActionType
import io.github.sds100.keymapper.Adapters.AppShortcutAdapter
import io.github.sds100.keymapper.Adapters.SimpleItemAdapter
import io.github.sds100.keymapper.R
import io.github.sds100.keymapper.ShortcutTitleDialog
import io.github.sds100.keymapper.Utils.AppShortcutUtils
import kotlinx.android.synthetic.main.action_type_recyclerview.*

/**
 * Created by sds100 on 31/07/2018.
 */

/**
 * A Fragment which shows all the available shortcut widgets for each installed app which has them
 */
class AppShortcutActionTypeFragment : ActionTypeFragment(),
        SimpleItemAdapter.OnItemClickListener<ResolveInfo> {

    companion object {
        private const val REQUEST_CODE_SHORTCUT_CONFIGURATION = 837
    }

    private val mAppShortcutAdapter by lazy {
        AppShortcutAdapter(
                context!!.packageManager, onItemClickListener = this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.action_type_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mAppShortcutAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SHORTCUT_CONFIGURATION &&
                resultCode == Activity.RESULT_OK) {

            if (data != null) {
                //the shortcut intents seem to be returned in 2 different formats.
                if (data.extras != null &&
                        data.extras!!.containsKey(Intent.EXTRA_SHORTCUT_INTENT)) {
                    //get intent from selected shortcut
                    val shortcutIntent = data.extras!!.get(Intent.EXTRA_SHORTCUT_INTENT) as Intent

                    //show a dialog to prompt for a title.
                    ShortcutTitleDialog.show(context!!) { title ->
                        shortcutIntent.putExtra(AppShortcutUtils.EXTRA_SHORTCUT_TITLE, title)

                        //save the shortcut intent as a URI
                        val action = Action(ActionType.APP_SHORTCUT, shortcutIntent.toUri(0))
                        chooseSelectedAction(action)
                    }

                } else {
                    ShortcutTitleDialog.show(context!!) { title ->
                        data.putExtra(AppShortcutUtils.EXTRA_SHORTCUT_TITLE, title)

                        //save the shortcut intent as a URI
                        val action = Action(ActionType.APP_SHORTCUT, data.toUri(0))
                        chooseSelectedAction(action)
                    }
                }
            }
        }
    }

    override fun onItemClick(item: ResolveInfo) {
        //open the shortcut configuration screen when the user taps a shortcut
        val intent = Intent()
        intent.setClassName(item.activityInfo.applicationInfo.packageName, item.activityInfo.name)
        startActivityForResult(intent, REQUEST_CODE_SHORTCUT_CONFIGURATION)
    }
}