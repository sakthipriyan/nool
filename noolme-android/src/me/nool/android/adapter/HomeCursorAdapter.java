package me.nool.android.adapter;

import me.nool.android.entity.EntryStatus;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;

public class HomeCursorAdapter extends SimpleCursorAdapter {

	private EntryStatus mStatus;
	
//	private int mLayout;
//	private Context mContext;
//	private Cursor mCursor;
//	private LayoutInflater mInflater;
	
//	private int mImageIndex;
//	private int mTimestampIndex;
//	private int mThreadNameIndex;
//	private int mContentIndex;
	
//	private final class ViewHolder {
//		ImageView imageView;
//		TextView threadNameView;
//		TextView contentTxtView;
//		TextView timestampView;
//	}

	public HomeCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags, EntryStatus status) {
		super(context, layout, c, from, to, flags);
		this.mStatus = status;
//		this.mContext = context;
//		this.mLayout = layout;
//		this.mInflater = LayoutInflater.from(context);
//		this.mCursor = c;
		
//		this.mImageIndex =  mCursor.getColumnIndex(NoolDB.KEY_ICON_URL);
//		this.mTimestampIndex =  mCursor.getColumnIndex(NoolDB.KEY_TIMESTAMP);
//		this.mThreadNameIndex =  mCursor.getColumnIndex(NoolDB.KEY_THREAD_NAME);
//		this.mContentIndex =  mCursor.getColumnIndex(NoolDB.KEY_CONTENT_TXT);


	}

/*
	@Override
	public void bindView(View v, Context context, Cursor c) {

		int nameCol = c.getColumnIndex(People.NAME);

		String name = c.getString(nameCol);

		*//**
		 * Next set the name of the entry.
		 *//*
		TextView name_text = (TextView) v.findViewById(R.id.name_entry);
		if (name_text != null) {
			name_text.setText(name);
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		Cursor c = getCursor();
		View v = mInflater.inflate(mLayout, parent, false);
		
		String threadName = c.getString(mThreadNameIndex);
		String contentTxt = c.getString(mContentIndex);
		String iconUrl = c.getString(mImageIndex);
		long timestamp = c.getLong(mTimestampIndex);
		


		*//**
		 * Next set the name of the entry.
		 *//*
		TextView name_text = (TextView) v.findViewById(R.id.name_entry);
		if (name_text != null) {
			name_text.setText(name);
		}

		return v;
	}
*/
	/*

	 *  @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
     

        String name = c.getString(c.getColumnIndex(WhipemDBAdapter.KEY_NAME));
        String fb_id = c.getString(c.getColumnIndex(WhipemDBAdapter.KEY_FB_ID));

        TextView name_text = (TextView) v.findViewById(R.id.contact_name);
        if (name_text != null) {
            name_text.setText(name);
        }

        ImageView im = (ImageView) v.findViewById(R.id.contact_pic);            
        Drawable drawable = LoadImageFromWebOperations("http://graph.facebook.com/"+fb_id+"/picture");
        if (im != null) {
            im.setImageDrawable(drawable);
        }

        CheckBox bCheck = (CheckBox) v.findViewById(R.id.checkbox);
        if (im != null) {
            bCheck.setTag(fb_id);
        }            

        if (((GlobalVars) mContext.getApplicationContext()).isFriendSelected(fb_id))
            bCheck.setChecked(true);

        bCheck.setOnClickListener(this);

        return v;
    }

    @Override
    public void bindView(View v, Context context, Cursor c) {

         String name = c.getString(c.getColumnIndex(WhipemDBAdapter.KEY_NAME));
         String fb_id = c.getString(c.getColumnIndex(WhipemDBAdapter.KEY_FB_ID));


         TextView name_text = (TextView) v.findViewById(R.id.contact_name);
         if (name_text != null) {
             name_text.setText(name);
         }

        ImageView im = (ImageView) v.findViewById(R.id.contact_pic);            
        Drawable drawable = LoadImageFromWebOperations("http://graph.facebook.com/"+fb_id+"/picture");
        if (im != null) {
            im.setImageDrawable(drawable);
        }

        CheckBox bCheck = (CheckBox) v.findViewById(R.id.checkbox);
        if (im != null) {
            bCheck.setTag(fb_id);
        }

        ArrayList<String> dude = ((GlobalVars) mContext.getApplicationContext()).getSelectedFriendList();

        if (((GlobalVars) mContext.getApplicationContext()).isFriendSelected(fb_id))
            bCheck.setChecked(true);

        bCheck.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        CheckBox cBox = (CheckBox) v;
        String fb_id = (String) cBox.getTag();

        if (cBox.isChecked()) {
            if (!((GlobalVars) mContext.getApplicationContext()).isFriendSelected(fb_id))
                ((GlobalVars) mContext.getApplicationContext()).addSelectedFriend(fb_id);
        } else {
            if (((GlobalVars) mContext.getApplicationContext()).isFriendSelected(fb_id))
                ((GlobalVars) mContext.getApplicationContext()).removeSelectedFriend(fb_id);
        }

    }

    private Drawable LoadImageFromWebOperations(String url)
    {
        try
        {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        }catch (Exception e) {
            System.out.println("Exc="+e);
            return null;
        }
    }

}


	 * private class ContentAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public ContentAdapter(Context context) {
			mInflater = LayoutInflater.from(context);

		}

		public int getCount() {
			return Home.this.entries.size();
		}

		public Object getItem(int position) {
			return entries.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.home_entry_view, null);
				holder = new ViewHolder();
				// holder.statusView = (View)
				// convertView.findViewById(R.id.entry_status);
				// holder.iconView = (ImageView)
				// convertView.findViewById(R.id.entry_icon);
				holder.threadView = (TextView) convertView
						.findViewById(R.id.entry_threadname);
				holder.timestampView = (TextView) convertView
						.findViewById(R.id.entry_timestamp);
				holder.contentView = (TextView) convertView
						.findViewById(R.id.entry_preview);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (position < entries.size()) {

				HomeEntry entry = entries.get(position);
				holder.timestampView.setText(entry.getTimestampAsString());
				holder.threadView.setText(entry.getThreadName());
				holder.contentView.setText(entry.getContentText());
			}
			return convertView;
		}

	}
	 * 
	 * 
	 * 
	 */
	
	
}
