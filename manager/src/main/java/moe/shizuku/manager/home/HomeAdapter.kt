package moe.shizuku.manager.home

import android.os.Process
import moe.shizuku.api.ShizukuService
import moe.shizuku.manager.ShizukuSettings
import moe.shizuku.manager.ShizukuSettings.LaunchMethod
import moe.shizuku.manager.management.AppsViewModel
import rikka.core.util.BuildUtils
import rikka.recyclerview.IdBasedRecyclerViewAdapter
import rikka.recyclerview.IndexCreatorPool
import java.util.*

class HomeAdapter(private val homeModel: HomeViewModel, private val appsModel: AppsViewModel) : IdBasedRecyclerViewAdapter(ArrayList()) {

    override fun onCreateCreatorPool(): IndexCreatorPool {
        return IndexCreatorPool()
    }

    fun updateData() {
        val status = homeModel.serviceStatus.value?.data ?: return
        val grantedCount = appsModel.grantedCount.value?.data ?: 0
        val running = ShizukuService.pingBinder()

        clear()
        addItem(ServerStatusViewHolder.CREATOR, status, 0)
        addItem(ManageAppsViewHolder.CREATOR, grantedCount, 1)
        if (Process.myUid() / 100000 == 0) {
            val root = ShizukuSettings.getLastLaunchMode() == LaunchMethod.ROOT
            val rootRestart = running && status.uid == 0
            when {
                root && BuildUtils.atLeast30 -> {
                    addItem(StartRootViewHolder.CREATOR, rootRestart, 3)
                    addItem(StartWirelessAdbViewHolder.CREATOR, null, 4)
                    addItem(StartAdbViewHolder.CREATOR, null, 2)
                }
                root && !BuildUtils.atLeast30 -> {
                    addItem(StartRootViewHolder.CREATOR, rootRestart, 3)
                    addItem(StartAdbViewHolder.CREATOR, null, 2)
                    addItem(StartWirelessAdbViewHolder.CREATOR, null, 4)
                }
                BuildUtils.atLeast30 -> {
                    addItem(StartWirelessAdbViewHolder.CREATOR, null, 4)
                    addItem(StartAdbViewHolder.CREATOR, null, 2)
                    addItem(StartRootViewHolder.CREATOR, rootRestart, 3)
                }
                else -> {
                    addItem(StartAdbViewHolder.CREATOR, null, 2)
                    addItem(StartWirelessAdbViewHolder.CREATOR, null, 4)
                    addItem(StartRootViewHolder.CREATOR, rootRestart, 3)
                }
            }
        }
        addItem(LearnMoreViewHolder.CREATOR, null, 100)
        notifyDataSetChanged()
    }

    init {
        updateData()
        setHasStableIds(true)
    }
}