
package com.rakamin.newsapp.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedSearchFilters(
    state: SearchUiState,
    onFromDateChange: (String?) -> Unit,
    onToDateChange: (String?) -> Unit,
    onSortByChange: (String) -> Unit,
    onSourcesChange: (String) -> Unit,
    onDomainsChange: (String) -> Unit,
    onApply: () -> Unit,
    onReset: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var sortMenuExpanded by remember { mutableStateOf(false) }

    val sortOptions = listOf(
        "publishedAt" to "Latest",
        "relevancy" to "Relevancy",
        "popularity" to "Popularity"
    )

    val selectedSort = sortOptions
        .firstOrNull { it.first == state.sortBy }
        ?.second ?: "Latest"

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextButton(
                onClick = { expanded = !expanded }
            ) {
                Text(
                    if (expanded) {
                        "Hide Advanced Filters"
                    } else {
                        "Advanced Filters"
                    }
                )
            }

            if (expanded) {
                Text(
                    text = "Date Range",
                    style = MaterialTheme.typography.titleSmall
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DatePickerField(
                        label = "From",
                        date = state.fromDate,
                        modifier = Modifier.weight(1f),
                        onDateSelected = onFromDateChange
                    )

                    DatePickerField(
                        label = "To",
                        date = state.toDate,
                        modifier = Modifier.weight(1f),
                        onDateSelected = onToDateChange
                    )
                }

                Text(
                    text = "Sort By",
                    style = MaterialTheme.typography.titleSmall
                )

                Box {
                    OutlinedButton(
                        onClick = { sortMenuExpanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Sort: $selectedSort")
                    }

                    DropdownMenu(
                        expanded = sortMenuExpanded,
                        onDismissRequest = {
                            sortMenuExpanded = false
                        }
                    ) {
                        sortOptions.forEach { (value, label) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    onSortByChange(value)
                                    sortMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Text(
                    text = "Publisher Filter",
                    style = MaterialTheme.typography.titleSmall
                )

                OutlinedTextField(
                    value = state.sources,
                    onValueChange = onSourcesChange,
                    label = { Text("Source IDs") },
                    placeholder = {
                        Text("Example: bbc-news,cnn")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.domains,
                    onValueChange = onDomainsChange,
                    label = { Text("Domains") },
                    placeholder = {
                        Text("Example: bbc.com,cnn.com")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onReset,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Reset")
                    }

                    Button(
                        onClick = onApply,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Apply Filters")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerField(
    label: String,
    date: String?,
    modifier: Modifier = Modifier,
    onDateSelected: (String?) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    OutlinedButton(
        onClick = { showDialog = true },
        modifier = modifier
    ) {
        Text("$label: ${date ?: "Select"}")
    }

    if (showDialog) {
        val initialDateMillis = date?.let {
            runCatching {
                LocalDate.parse(it)
                    .atStartOfDay(ZoneOffset.UTC)
                    .toInstant()
                    .toEpochMilli()
            }.getOrNull()
        }

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialDateMillis
        )

        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedDate = Instant
                                .ofEpochMilli(millis)
                                .atZone(ZoneOffset.UTC)
                                .toLocalDate()
                                .toString()

                            onDateSelected(selectedDate)
                        }

                        showDialog = false
                    }
                ) {
                    Text("Select")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
