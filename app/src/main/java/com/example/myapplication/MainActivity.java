package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerCategory, spinnerFrom, spinnerTo;
    EditText etInput;
    Button btnConvert;
    TextView tvResult;

    String[] categories = {"Currency", "Fuel", "Liquid Volume", "Temperature"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerFrom = findViewById(R.id.spinnerFrom);
        spinnerTo = findViewById(R.id.spinnerTo);
        etInput = findViewById(R.id.etInput);
        btnConvert = findViewById(R.id.btnConvert);
        tvResult = findViewById(R.id.tvResult);

        setSpinnerData(spinnerCategory, categories);
        updateUnitSpinners();
        updateButtonColor();

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateUnitSpinners();
                updateButtonColor();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double value = Double.parseDouble(etInput.getText().toString());

                String category = spinnerCategory.getSelectedItem().toString();
                String fromUnit = spinnerFrom.getSelectedItem().toString();
                String toUnit = spinnerTo.getSelectedItem().toString();

                double result;

                if (category.equals("Currency")) {
                    result = convertCurrency(value, fromUnit, toUnit);
                } else if (category.equals("Fuel")) {
                    result = convertFuel(value, fromUnit, toUnit);
                } else if (category.equals("Liquid Volume")) {
                    result = convertVolume(value, fromUnit, toUnit);
                } else {
                    result = convertTemperature(value, fromUnit, toUnit);
                }

                tvResult.setText(String.format("Result: %.2f %s", result, toUnit));
            }
        });
    }

    private void setSpinnerData(Spinner spinner, String[] items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void updateUnitSpinners() {
        String selectedCategory = spinnerCategory.getSelectedItem() != null
                ? spinnerCategory.getSelectedItem().toString()
                : "Currency";

        String[] units;

        if (selectedCategory.equals("Currency")) {
            units = new String[]{"USD", "AUD", "EUR", "JPY", "GBP"};
        } else if (selectedCategory.equals("Fuel")) {
            units = new String[]{"mpg", "km/L"};
        } else if (selectedCategory.equals("Liquid Volume")) {
            units = new String[]{"Gallon (US)", "Liters"};
        } else {
            units = new String[]{"Celsius", "Fahrenheit", "Kelvin"};
        }

        setSpinnerData(spinnerFrom, units);
        setSpinnerData(spinnerTo, units);

        if (units.length > 1) {
            spinnerTo.setSelection(1);
        }
    }

    private void updateButtonColor() {
        String selectedCategory = spinnerCategory.getSelectedItem() != null
                ? spinnerCategory.getSelectedItem().toString()
                : "Currency";

        if (selectedCategory.equals("Currency")) {
            btnConvert.setBackgroundColor(Color.parseColor("#2E7D32"));
        } else if (selectedCategory.equals("Fuel")) {
            btnConvert.setBackgroundColor(Color.parseColor("#1565C0"));
        } else if (selectedCategory.equals("Liquid Volume")) {
            btnConvert.setBackgroundColor(Color.parseColor("#6A1B9A"));
        } else {
            btnConvert.setBackgroundColor(Color.parseColor("#C62828"));
        }
    }

    private double convertCurrency(double value, String from, String to) {
        double usd = 0;

        if (from.equals("USD")) {
            usd = value;
        } else if (from.equals("AUD")) {
            usd = value / 1.55;
        } else if (from.equals("EUR")) {
            usd = value / 0.92;
        } else if (from.equals("JPY")) {
            usd = value / 148.50;
        } else if (from.equals("GBP")) {
            usd = value / 0.78;
        }

        if (to.equals("USD")) {
            return usd;
        } else if (to.equals("AUD")) {
            return usd * 1.55;
        } else if (to.equals("EUR")) {
            return usd * 0.92;
        } else if (to.equals("JPY")) {
            return usd * 148.50;
        } else {
            return usd * 0.78;
        }
    }

    private double convertFuel(double value, String from, String to) {
        if (from.equals("mpg") && to.equals("km/L")) {
            return value * 0.425;
        } else {
            return value / 0.425;
        }
    }

    private double convertVolume(double value, String from, String to) {
        if (from.equals("Gallon (US)") && to.equals("Liters")) {
             return value * 3.785;
        } else {
            return value / 3.785;
        }
    }

    private double convertTemperature(double value, String from, String to) {
        if (from.equals("Celsius") && to.equals("Fahrenheit")) {
            return (value * 1.8) + 32;
        } else if (from.equals("Fahrenheit") && to.equals("Celsius")) {
            return (value - 32) / 1.8;
        } else if (from.equals("Celsius") && to.equals("Kelvin")) {
            return value + 273.15;
        } else if (from.equals("Kelvin") && to.equals("Celsius")) {
            return value - 273.15;
        } else if (from.equals("Fahrenheit") && to.equals("Kelvin")) {
            return ((value - 32) / 1.8) + 273.15;
        } else if (from.equals("Kelvin") && to.equals("Fahrenheit")) {
            return ((value - 273.15) * 1.8) + 32;
        } else {
            return value;
        }
    }
}