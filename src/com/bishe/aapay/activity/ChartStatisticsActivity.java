package com.bishe.aapay.activity;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;

import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import com.bishe.aapay.dao.AApayDBHelper;
import com.bishe.aapay.dao.ConsumptionDao;
import com.bishe.aapay.dao.PersonalBudgetDAO;

import android.R.anim;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class ChartStatisticsActivity extends Activity {

	/** Colors to be used for the pie slices. */
	private static int[] COLORS = new int[] { Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN };
	/** The main series that will include all the data. */
	private CategorySeries mSeries = new CategorySeries("");
	/** The main renderer for the main dataset. */
	private DefaultRenderer mRenderer = new DefaultRenderer();
	/** Button for adding entered data to the current series. */
	/** The chart view that displays the data. */
    private GraphicalView mChartView;
	private ActionBar actionBar;
	
	private AApayDBHelper dbHelper;
	private ConsumptionDao consumptionDao;
	private PersonalBudgetDAO personalBudgetDAO;
	private List<Map<String, String>> items = null;
	
	private Spinner spinnerCategotyType;
	private int selectedType = 1;
	private String [] types= {
			"AA消费","支出类别","收入类别"
	};
	
	  @Override
	  protected void onRestoreInstanceState(Bundle savedState) {
	    super.onRestoreInstanceState(savedState);
	    mSeries = (CategorySeries) savedState.getSerializable("current_series");
	    mRenderer = (DefaultRenderer) savedState.getSerializable("current_renderer");
	  }

	  @Override
	  protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    outState.putSerializable("current_series", mSeries);
	    outState.putSerializable("current_renderer", mRenderer);
	  }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(R.string.chart_statistics_activity);
		setContentView(R.layout.activity_chart_statistics);
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		dbHelper = new AApayDBHelper(this);
		consumptionDao = new ConsumptionDao(dbHelper);
		personalBudgetDAO = new PersonalBudgetDAO(dbHelper);
		
		initView();
		
		
		
	    /*
		 * 选择不同的类别查看
		 */
		spinnerCategotyType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				selectedType = position + 1;
				Toast.makeText(ChartStatisticsActivity.this, types[position]+" "+selectedType, Toast.LENGTH_SHORT).show();
				//AA消费
				if(selectedType == 1) {
					items = consumptionDao.getConsumptionByType();
				}
				//支出
				else if(selectedType == 2) {
					items = personalBudgetDAO.getBudgetByType(selectedType);
				}
				//收入
				else if(selectedType == 3) {
					items = personalBudgetDAO.getBudgetByType(selectedType);
				}
				countPercent();
				mSeries.clear();
				mRenderer.removeAllRenderers();
				for(Map<String,String> map : items) {
			    	mSeries.add(map.get("type"), Double.parseDouble(map.get("sept_money")));
			        SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			        renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
			        mRenderer.addSeriesRenderer(renderer);
			    }
				if(mChartView != null) {
					 mChartView.repaint();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
				
			}
		});
	}
	
	private void initView() {
		spinnerCategotyType = (Spinner) findViewById(R.id.category_spinner_type);
		spinnerCategotyType.setAdapter(new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, types));
		mRenderer.setZoomButtonsVisible(true);
	    mRenderer.setStartAngle(180);
	    mRenderer.setDisplayValues(true);
	    mRenderer.setLabelsColor(Color.BLACK);
	    mRenderer.setLabelsTextSize(15);
	}
	  @Override
	  protected void onResume() {
	    super.onResume();
	    if (mChartView == null) {
	      LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
	      mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
	      
	      mRenderer.setClickEnabled(true);
	      mChartView.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	          SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
	          if (seriesSelection == null) {
	            Toast.makeText(ChartStatisticsActivity.this, "No chart element selected", Toast.LENGTH_SHORT)
	                .show();
	          } else {
	            for (int i = 0; i < mSeries.getItemCount(); i++) {
	              mRenderer.getSeriesRendererAt(i).setHighlighted(i == seriesSelection.getPointIndex());
	            }
	            mChartView.repaint();
	            Toast.makeText(
	            		ChartStatisticsActivity.this,
	            		mSeries.getCategory(seriesSelection.getPointIndex()) +"："
	            		+ formatDouble(seriesSelection.getValue())+"  比例："+items.get(seriesSelection.getPointIndex()).get("percent"), 
	            		Toast.LENGTH_SHORT).show();
	          }
	        }
	      });
	      layout.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT,
	          LayoutParams.MATCH_PARENT));
	    } else {
	      mChartView.repaint();
	    }
	  }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * 计算百分比
	 */
	private  void countPercent() {
		double sum = 0;
		for(Map<String,String> map : items) {
			sum += Double.parseDouble(map.get("sept_money"));
	    }
		DecimalFormat df = new DecimalFormat("##.00%");
		for(Map<String,String> map : items) {
			double money = Double.parseDouble(map.get("sept_money"));
			double percent = money / sum;
			map.put("percent", df.format(percent));
	    }
	}
	
	/**
	 * 格式化浮点数
	 * @param value
	 * @return
	 */
	private String formatDouble(double value) {
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(value);
	}
}
