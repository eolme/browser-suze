package de.baumann.browser.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import de.baumann.browser.Database.Record;
import de.baumann.browser.Ninja.R;

public class CompleteAdapter extends BaseAdapter implements Filterable {
    @NonNull
    private final Context context;
    @LayoutRes
    private final int layoutResId;
    @NonNull
    private final List<CompleteItem> originalList;
    @NonNull
    private final List<CompleteItem> resultList;

    public CompleteAdapter(@NonNull Context context, @NonNull List<Record> recordList) {
        this.context = context;
        this.layoutResId = R.layout.list_item;
        this.originalList = new ArrayList<>();
        this.resultList = new ArrayList<>();
        deDup(recordList);
    }

    private void deDup(@NonNull List<Record> recordList) {
        for (@NonNull Record record : recordList) {
            if (record.getTitle() != null
                    && !record.getTitle().isEmpty()
                    && record.getURL() != null
                    && !record.getURL().isEmpty()) {
                originalList.add(new CompleteItem(record.getTitle(), record.getURL()));
            }
        }

        Set<CompleteItem> set = new HashSet<>(originalList);
        originalList.clear();
        originalList.addAll(set);
    }

    @Override
    @NonNull
    public Filter getFilter() {
        return filter;
    }

    private final CompleteFilter filter = new CompleteFilter();

    @Override
    @NonNull
    public Object getItem(int position) {
        return resultList.get(position);
    }

    @Override
    @Nullable
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        Holder holder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(layoutResId, null, false);
            holder = new Holder();
            holder.titleView = view.findViewById(R.id.record_item_title);
            holder.urlView = view.findViewById(R.id.record_item_url);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        CompleteItem item = resultList.get(position);
        holder.titleView.setText(item.getTitle());
        if (item.getURL() != null) {
            holder.urlView.setText(item.getURL());
        } else {
            holder.urlView.setText(item.getURL());
        }

        return view;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    private static class Holder {
        AppCompatTextView titleView;
        AppCompatTextView urlView;
    }

    private class CompleteFilter extends Filter {
        @Override
        @NonNull
        protected FilterResults performFiltering(@Nullable CharSequence prefix) {
            if (prefix == null) {
                return new FilterResults();
            }

            resultList.clear();

            String title;
            String url;
            for (@NonNull CompleteItem item : originalList) {
                title = item.getTitle();
                url = item.getURL();

                if (title != null && url != null) {
                    if (title.contains(prefix)) {
                        item.setIndex(title.indexOf(prefix.toString()));
                        resultList.add(item);
                    } else if (url.contains(prefix)) {
                        item.setIndex(url.indexOf(prefix.toString()));
                        resultList.add(item);
                    }
                }
            }

            Collections.sort(resultList, (first, second) ->
                    Integer.compare(first.getIndex(), second.getIndex()));

            FilterResults results = new FilterResults();
            results.values = resultList;
            results.count = resultList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class CompleteItem {
        @Nullable
        private final String title;
        @Nullable
        private final String url;

        CompleteItem(@Nullable String title, @Nullable String url) {
            this.title = title;
            this.url = url;
        }

        @Nullable
        String getTitle() {
            return title;
        }

        private int index = Integer.MAX_VALUE;

        int getIndex() {
            return index;
        }

        void setIndex(int index) {
            this.index = index;
        }

        @Nullable
        String getURL() {
            return url;
        }

        @Contract(value = "null -> false", pure = true)
        @Override
        public boolean equals(@Nullable Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof CompleteItem)) {
                return false;
            }

            final CompleteItem item = (CompleteItem) object;
            return Objects.equals(item.getTitle(), title) &&
                    Objects.equals(item.getURL(), url);
        }

        @Override
        public int hashCode() {
            if (title == null || url == null) {
                return 0;
            }

            return title.hashCode() & url.hashCode();
        }
    }
}
