package budget.homebank.monsieur_h.homebudget.activities;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;
import budget.homebank.monsieur_h.homebudget.R;
import budget.homebank.monsieur_h.homebudget.homebank.Category;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Implementation of App Widget functionality.
 */
public class HomeBankWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.home_bank_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        int month = Calendar.getInstance().getTime().getMonth();
        List<Category> topCategoriesForMonthlyBudget = BudgetSummaryActivity.HOMEBANK_MAPPER.getTopCategoriesForMonthlyBudget(month);
        float sumRatio = 0;
        for (Category category : topCategoriesForMonthlyBudget) {
            sumRatio += category.getMonthlyExpenseRatio(month);
        }

        float progress = sumRatio / topCategoriesForMonthlyBudget.size() * 100;
        views.setProgressBar(R.id.widget_progressbar, 100, (int) progress, false);

        String monthName = (String) android.text.format.DateFormat.format("MMMM", Calendar.getInstance().getTime());
        String widgetMessage = String.format(Locale.getDefault(), "%s %s : %.2f %%", context.getString(R.string.widget_budget_message), monthName, progress);
        views.setTextViewText(R.id.appwidget_text, widgetMessage);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

