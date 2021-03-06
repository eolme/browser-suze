/*
 * Open source android application.
 *
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

package website.petrov.browser.unit;

import android.database.Cursor;

import androidx.annotation.NonNull;

public class DatabaseUnit {
    @NonNull
    public static String getSafeString(@NonNull Cursor cursor, @NonNull String name) {
        if (cursor.isClosed()) {
            return ""; // Cursor is already closed
        }
        if (cursor.getPosition() == -1 && !cursor.moveToFirst()) {
            return ""; // No data available
        }
        int column = cursor.getColumnIndex(name);
        if (column == -1) {
            return ""; // Column not found
        }
        String cell = cursor.getString(column);
        return cell == null ? "" : cell; // Column can be nullable
    }
}
