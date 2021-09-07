package com.ar.team.company.app.whatsdelete.ui.activity.applications;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ar.team.company.app.whatsdelete.model.Application;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class ApplicationsViewModel extends AndroidViewModel {
    PackageManager manager;
    // Fields:
    private final MutableLiveData<List<Application>> data = new MutableLiveData<>();
    private final LiveData<List<Application>> applications = data;

    // Def constructor for context:
    public ApplicationsViewModel(@NonNull @NotNull android.app.Application application) {
        super(application);
    }

    // Operations:
    public LiveData<List<Application>> getAppsModel(Context context) {


        // Initializing:
        List<Application> apps = new ArrayList<>(); // The returning or setting apps list.
         manager = getApplication().getPackageManager(); // Getting the current package manager.

        @SuppressLint("QueryPermissionsNeeded")
        List<ApplicationInfo> packages  = checkForLaunchIntent(manager.getInstalledApplications(PackageManager.GET_META_DATA));
        List<PackageInfo> packs = manager.getInstalledPackages(0);

        for (PackageInfo packageInfo : packs) { // For loop on list of applications info.
            // Initializing:
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) ==0  ){

            String appName = packageInfo.applicationInfo.loadLabel(manager).toString(); // Getting app name.
            Drawable appIcon = packageInfo.applicationInfo.loadIcon(manager); // Getting app icon as drawable.
            Drawable appLogo = packageInfo.applicationInfo.loadIcon(manager);
            // Developing:

            apps.add(new Application(appName, packageInfo.packageName, appIcon, appLogo));// add the new app.

            }
        }


        // Setting the apps values:
        data.setValue(apps);
        // Returning:
        return applications;
    }

    // Getters:
    public MutableLiveData<List<Application>> getData() {
        return data;
    }

    public LiveData<List<Application>> getApplications() {
        return applications;
    }

    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) {
        ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
        for (ApplicationInfo info : list) {
            try {
                if (null != manager.getLaunchIntentForPackage(info.packageName)) {
                    applist.add(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return applist;
    }
}
