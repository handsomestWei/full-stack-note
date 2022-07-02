# Create the data for the chart.
H <- c(7,12,28,3,41)

# Give the chart file a name.
png(file = "D:\barchart.png")

# Plot the bar chart.
barplot(H)

# Save the file.
dev.off()