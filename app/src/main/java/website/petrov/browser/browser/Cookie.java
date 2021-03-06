/*
 * Open source android application.
 *
 * Copyright (c) 2017 Gaukler Faun
 * Copyright (c) 2019 Petrov Anton
 *
 * This file is part of Suze Browser.
 *
 * Suze Browser is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Suze Browser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Suze Browser. If not, see <https://www.gnu.org/licenses/>.
 */

package website.petrov.browser.browser;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import website.petrov.browser.database.RecordAction;

public class Cookie {
    private static final String TAG = "Cookie";

    private static final String FILE = "cookieHosts.txt";
    private static final Set<String> hostsCookie = new HashSet<>();
    private static final List<String> whitelistCookie = new ArrayList<>();
    @NonNull
    private final Context context;

    public Cookie(@NonNull Context context) {
        this.context = context;

        if (hostsCookie.isEmpty()) {
            loadHosts(context);
        }
        loadDomains(context);
    }

    private static void loadHosts(@NonNull Context context) {
        Thread thread = new Thread(() -> {
            AssetManager manager = context.getAssets();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(manager.open(FILE)));
                String line;
                while ((line = reader.readLine()) != null) {
                    hostsCookie.add(line.toLowerCase(Locale.getDefault()));
                }
            } catch (IOException i) {
                Log.e(TAG, "Error loading hosts", i);
            }
        });
        thread.start();
    }

    private synchronized static void loadDomains(@NonNull Context context) {
        RecordAction action = new RecordAction(context);
        action.open(false);
        whitelistCookie.clear();
        whitelistCookie.addAll(action.listDomainsCookie());
        action.close();
    }

    public boolean isWhite(@NonNull String url) {
        for (String domain : whitelistCookie) {
            if (url.contains(domain)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void addDomain(@NonNull String domain) {
        RecordAction action = new RecordAction(context);
        action.open(true);
        action.addDomainCookie(domain);
        action.close();
        whitelistCookie.add(domain);
    }

    public synchronized void removeDomain(@NonNull String domain) {
        RecordAction action = new RecordAction(context);
        action.open(true);
        action.deleteDomainCookie(domain);
        action.close();
        whitelistCookie.remove(domain);
    }

    public synchronized void clearDomains() {
        RecordAction action = new RecordAction(context);
        action.open(true);
        action.clearDomainsCookie();
        action.close();
        whitelistCookie.clear();
    }
}
