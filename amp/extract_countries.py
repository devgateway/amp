import csv

def extract_country_data(csv_file_path):
    country_data = []

    with open(csv_file_path, 'r') as file:
        reader = csv.DictReader(file)

        for row in reader:
            # Assuming 'longitude', 'latitude', and 'COUNTRY' are the column names
            longitude = float(row['longitude'])
            latitude = float(row['latitude'])
            country = row['COUNTRY']
            if("'" in country):
                continue

            print((country, str(longitude), str(latitude)),end="," )

    return country_data

# Example usage:
csv_file_path = 'countries.csv'
country_list = extract_country_data(csv_file_path)
