// IXMInterface.aidl
package me.jiangnight.xsettingmanager;

import android.content.pm.PackageInfo;
import rikka.parcelablelist.ParcelableListSlice;


interface IXMInterface {
        ParcelableListSlice<PackageInfo> getPackages(int flags);
}