package com.georgeadaimi.scoutguide;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CampSiteFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
	TextView beirutButton;
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static CampSiteFragment newInstance(int sectionNumber) {
    	CampSiteFragment fragment = new CampSiteFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public CampSiteFragment(){
    	
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.campsite_fragment, container, false);
        rootView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Touch(v,event);
				return false;
			}
            
    });
        //getActivity().getActionBar().setTitle("Camp Sites");
        return rootView;
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        ((MainActivity) activity).onSectionAttached(
//                getArguments().getInt(ARG_SECTION_NUMBER));
//    }
    
    public boolean Touch (View v, MotionEvent ev) {
    	Log.d("heyyy", "hgjhgj");
    	 final int action = ev.getAction();
    	 // (1) 
    	 final int evX = (int) ev.getX();
    	 final int evY = (int) ev.getY();
    	 ImageView imageView = (ImageView) v.findViewById (R.id.mapLebanon);
//    	    if (imageView == null) return false;
    	 Integer tagNum = (Integer) imageView.getTag ();
    	    int currentResource = (tagNum == null) ? R.drawable.lebanonmap1 : tagNum.intValue ();
    	 switch (action) {
    	 case MotionEvent.ACTION_DOWN :
    		 int touchColor = getHotspotColor (v,R.id.mapLebanon, evX, evY);
      	   // Compare the touchColor to the expected values. 
      	   // Switch to a different image, depending on what color was touched.
      	   // Note that we use a Color Tool object to test whether the 
      	   // observed color is close enough to the real color to
      	   // count as a match. We do this because colors on the screen do 
      	   // not match the map exactly because of scaling and
      	   // varying pixel density.
			 Log.d("TouchColor: ", Integer.toString(touchColor));
      	   ColorTool ct = new ColorTool ();
      	   int tolerance = 25;
      	   // (3)
      	   int colorRed = -4382464;
      	   int colorOrange = -1077248;
      	   int colorGreen=-16732928;
      	   int colorBlue=-16739180;
      	   //int colorBeirut=-59368;
			 int colorBeirut=-3158755;
      	 Fragment fragment =null;
	      	 FragmentManager fragmentManager = getActivity().getFragmentManager();
	         FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
      	   if (ct.closeMatch (colorRed, touchColor, tolerance)) {
      	      // Do the action associated with the RED region
      		 fragment = new MntLebList();
             
      	   } 
      	   else if(ct.closeMatch (colorGreen, touchColor, tolerance)) {
      		fragment = new BeqaaList();
      	     
      	   }else if(ct.closeMatch (colorOrange, touchColor, tolerance)) {
			   fragment = new NthLebList();
      	     
    	   }else if(ct.closeMatch (colorBlue, touchColor, tolerance)) {
    		   fragment = new SthLebList();
        	     
        	   }else if(ct.closeMatch (colorBeirut, touchColor, tolerance)) {
    		   fragment = new BeirutList();
        	     
        	   }
      	   if(fragment !=null)
      	   {
      		 fragmentTransaction.replace(R.id.container, fragment);
             fragmentTransaction.addToBackStack(null);
             
             fragmentTransaction.commit();
      	   }
      	   break;
      	  
    	 case MotionEvent.ACTION_UP :
//    	   // On the UP, we do the click action.
//    	   // The hidden image (image_areas) has three different hotspots on it.
//    	   // The colors are red, blue, and yellow.
//    	   // Use image_areas to determine which region the user touched.
//    	   // (2)
//    	   int touchColor = getHotspotColor (v,R.id.mapLebanon, evX, evY);
//    	   // Compare the touchColor to the expected values. 
//    	   // Switch to a different image, depending on what color was touched.
//    	   // Note that we use a Color Tool object to test whether the 
//    	   // observed color is close enough to the real color to
//    	   // count as a match. We do this because colors on the screen do 
//    	   // not match the map exactly because of scaling and
//    	   // varying pixel density.
//    	   ColorTool ct = new ColorTool ();
//    	   int tolerance = 25;
//    	   // (3)
//    	  
    	   break;
    	  } // end switch
    	  return true;
    	}
    public int getHotspotColor (View v, int hotspotId, int x, int y) {
        ImageView img = (ImageView) v.findViewById (hotspotId);
        if (img == null) {
           Log.d ("ImageAreasActivity", "Hot spot image not found");
           return 0;
        } else {
          img.setDrawingCacheEnabled(true); 
          Bitmap hotspots = Bitmap.createBitmap(img.getDrawingCache()); 
          if (hotspots == null) {
             Log.d ("ImageAreasActivity", "Hot spot bitmap was not created");
             return 0;
          } else {
            img.setDrawingCacheEnabled(false);
            return hotspots.getPixel(x, y);
          }
        }
    }
    
}
