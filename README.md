# DateFormatter

Simple ISO 8601 date formatting library for Android. 

## Usage

### Basic Usage
```
String dateTime = "1904-01-01T00:00:00+00:00"

Date date = DateFormatter.from(dateTime).toDate();

String date = DateFormatter.from(dateTime).to(FULL_MONTH); // 1 January 1904
...
```

### Date Comparison
Compare two dates. Can compare ISO 8601 date string's (or any DateFormatter string output) to a java.Date object or another string.
```
String dateTime = "1904-01-01T00:00:00+00:00"
Date date = new Date();

DateFormatter.isDatePastOrFuture(dateTime, date);
// returns +1 if date 1 is in the future of date 2
// return -1 if date 1 is the the past of date 2
// returns 0 if dates are exactly the same
// returns -2 otherwise
```

## DateFormatter.Type Enum

Use the Type enum to format a date or string

```
FULL_MONTH("dd MMMM yyyy"),
FULL_MONTH_TIME_12("dd MMMM yyyy' - 'h:mm aa"),
FULL_MONTH_TIME_24("dd MMMM yyyy' - 'HH:mm"),
SHORT_MONTH("dd MMM yyyy"),
SHORT_MONTH_TIME_12("dd MMM yyyy' - 'h:mm aa"),
SHORT_MONTH_TIME_24("dd MMM yyyy' - 'HH:mm"),
TIME_12("h:mm aa"),
TIME_24("HH:mm"),
NO_YEAR_SHORT("dd MMM"),
NO_YEAR_FULL("dd MMMM"),
NO_DAY("MMMM yyyy"),
NO_DAY_SHORT("MMM yy"),
MONTH_ONLY_SHORT("MMM"),
MONTH_ONLY_FULL("MMMM"),
DAY_ONLY("d"),
NO_DAY_SHORT_MONTH("MMM yyyy"),
NO_DAY_SHORT_YEAR("MMMM yy"),

SERVER("yyyy-MM-dd'T'HH:mm:ss"),
SERVER_TIMEZONE("yyyy-MM-dd'T'HH:mm:ssZZZZZ"),
SERVER_NO_TIME("yyyy-MM-dd");
```
