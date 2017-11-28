package org.ipforsmartobjects.apps.volumekeys.configuration;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ipforsmartobjects.apps.volumekeys.databinding.ColorItemBinding;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ColorPaletteAdapter extends RecyclerView.Adapter<ColorPaletteAdapter.ViewHolder> {

    private List<Integer> mRgbColors;
    SimpleWidgetConfigActivity.ColorItemListener mItemListener;

    public ColorPaletteAdapter(List<Integer> colors, SimpleWidgetConfigActivity.ColorItemListener listener) {
        setList(colors);
        mItemListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        ColorItemBinding binding = ColorItemBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding, mItemListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Integer color = mRgbColors.get(position);
        viewHolder.mListItemRgbColorsBinding.colorSwatch.setBackgroundColor(color);
    }

    public void replaceData(List<Integer> colors) {
        setList(colors);
        notifyDataSetChanged();
    }

    private void setList(List<Integer> colors) {
        mRgbColors = checkNotNull(colors);
    }

    @Override
    public int getItemCount() {
        return mRgbColors.size();
    }

    private Integer getItem(int position) {
        return mRgbColors.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ColorItemBinding mListItemRgbColorsBinding;
        SimpleWidgetConfigActivity.ColorItemListener mItemListener;

        public ViewHolder(ColorItemBinding binding, SimpleWidgetConfigActivity.ColorItemListener itemListener) {
            super(binding.getRoot());
            mListItemRgbColorsBinding = binding;
            mItemListener = itemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Integer color = getItem(position);
            mItemListener.onColorClick(color);
        }
    }
}
