package de.fhbingen.mensa;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import de.fhbingen.mensa.Fragments.ListFragment;


public class MainActivity extends FragmentActivity {

	//public static boolean roleChanged;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_pager);
        Log.i(TAG, "ContentView is setted");

        //Connect to Mensa
        mensa = (Mensa) getApplication();
                
        //Setting the ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());

        
        // The PagerTabStripe is set automatically!

        //Start LoadWeekTask, Save to Map. Provide Access to this Map
        context = viewPager.getContext();
        new LoadWeekTask().execute(Mensa.APIURL + "getWeek=" + Mensa.getCurrentWeek());
        
        //Connection between viewpager und fragmentadapter (made in onPostExecute in Task)
        //viewPager.setAdapter(myFragmentPagerAdapter);

        
        
        
        
        
        /*
        listview = (ListView) findViewById(android.R.id.list);

		mensa = (Mensa) this.getApplication();

		// Load userrole from preferences
		SharedPreferences settings = getSharedPreferences(Mensa.PREF_USER, 0);
        Mensa.userRole = Mensa.UserRole.values()[settings.getInt("userRole", Mensa.UserRole.STUDENT.ordinal())];


        new LoadWeekTask().execute(Mensa.APIURL + "getWeek=201403");

        listview.setOnItemClickListener(new ListView.OnItemClickListener( ) {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent detail = new Intent(getApplicationContext(), DishDetailActivity.class);
                detail.putExtra("data", (Dish)listview.getItemAtPosition(position));
                startActivity(detail);
            }
        });
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent settings = new Intent(this, SettingsActivity.class);
			//startActivity(settings);

			startActivityForResult(settings, 1337);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1337 && resultCode == 0 && roleChanged){
			//TODO: Update view
			roleChanged = false;
		}
	}

	private void createList(){
    dlist = mensa.getDay("2014-01-13");

	    try {
            // Filling the Adapter with the generated values
            adapter = new DishItemAdapter(
                    this,
                    dlist
            );

            // Connection between ListView and Adapter
            listview.setAdapter(adapter);

	    } catch (Exception e) {
	      Log.e(TAG, "Exception cause: " + e.getCause() + "\nException message" +e.getMessage() + "\nException toStr" + e.toString());
	    }

	    // Set date
	    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
	    SimpleDateFormat dayFormat   = new SimpleDateFormat("EEEE", Locale.GERMAN);
	    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMMMMMMM", Locale.GERMAN);
	    try {
			Date date = inputFormat.parse("2014-01-13");

			Calendar cal = new GregorianCalendar();
			cal.setTime(date);

			TextView tv = (TextView) findViewById(R.id.textView_date);
			tv.setText(
				String.format(
					"%s, %2d. %s %d",
					dayFormat.format(date),
					cal.get(Calendar.DAY_OF_MONTH),
					monthFormat.format(date),
					cal.get(Calendar.YEAR)
				)
			);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class LoadWeekTask extends ContentTask {
		@Override
		protected void onPreExecute() {
			d = new ProgressDialog(MainActivity.this);
			d.setCancelable(false);
			d.setMessage("Lade Woche");
			d.show();
		}

		@Override
		protected void onPostExecute(String result) {
			mensa.loadWeek(result);
			createList(); //TODO: with current day
			d.dismiss();
		}

		private ProgressDialog d;
	*/
	}

    public CharSequence getPageTitle(int position) {
        return "Page " + (position + 1);
    }

    private static class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int index){
        	Log.d(TAG, "getItem( index : " + index + " )");
        	final Calendar rightNow = Calendar.getInstance();
        	rightNow.add(Calendar.DAY_OF_MONTH, index);
            // Computating from the actual day
            String date = Mensa.toYYYYMMDD(rightNow); // YYYY-MM-DD
            
            //TODO: Hier können NullPointer kommen wenn date nicht in der Map enthalten ist.
            //Tobi macht das noch =)
            return ListFragment.newInstance(date);
        }

        @Override
        public int getCount(){
            return NUMBER_OF_PAGES;
        }

        public CharSequence getPageTitle(int position){
        	final Calendar rightNow = Calendar.getInstance();
        	if(position == 0){
        		return "Heute";
        	} else if (position == 1) {
        		return "Morgen";
        	} else {
        		rightNow.add(Calendar.DAY_OF_MONTH, position);   		
        		return Mensa.toDDMMYYYY(rightNow); // DD.MM.YYYY
        	}
        }
    }

	private Mensa mensa;
    private List<Dish> dlist;
    private DishItemAdapter adapter;
    private ListView listview;
    private ViewPager viewPager;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private static String today = "2014-03-10";
    private final static String TAG = "MainActivity";
    // TODO perhaps computating the max value of available pages
    private final static int NUMBER_OF_PAGES = 5;
    
    /**
     * Task loads a week
     * @author tknapp@fh-bingen.de
     *
     */
    private class LoadWeekTask extends ContentTask {
        @Override
        protected void onPreExecute() {
            d = new ProgressDialog(context);
            d.setCancelable(false);
            d.setMessage("Lade Speiseplan");
            d.show();
        }

        @Override
        protected void onPostExecute(String result) {
            mensa.loadWeek(result);
            d.dismiss();
            // Connection between viewpager und fragmentadapter
            viewPager.setAdapter(myFragmentPagerAdapter);
        }

        private ProgressDialog d;
    }
    
    private Context context;

}
