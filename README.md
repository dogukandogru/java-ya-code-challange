
# Java YA Code Challange

This is a console project written in Java which returns the recorded Earthquakes of a given
Country for the past {x} days.


# Usage


## Usage

1- Run the application

2- Input: Country, Count of days

Country: The Country whose earthquakes you want listed.

Count of days:  Earthquakes of a given Country for the past {x} days


## Examples

- Input:\
  `Turkey, 20`

* Output:\
  `Country	|	Place of Earthquake	|	Magnitude	|	Date	|	Time`\
  `Turkey	|	eastern Turkey	|	4.8	|	2022-09-16	|	23:30:36.971`\
  `Turkey	|	Western Turkey	|	4.3	|	2022-09-05	|	16:45:56.17`\
  `Turkey	|	eastern Turkey	|	4.3	|	2022-09-05	|	11:49:15.761`		
	
- Input:\
  `New Zeland, 10`

* Output:\
  `Country	|	Place of Earthquake	|	Magnitude	|	Date	|	Time`\
  `New Zeland	|	Mid-Indian Ridge	|	4.9	|	2022-09-21	|	06:01:09.713`\
  `New Zeland	|	null	|	4.9	|	2022-09-21	|	05:50:13.755`\
  `New Zeland	|	23 km NNW of Cartagena, Chile	|	4.0	|	2022-09-20	|	02:55:20.504`\		
  `New Zeland	|	Mid-Indian Ridge	|	5.6	|	2022-09-19	|	20:53:45.517`\		
  `New Zeland	|	Mid-Indian Ridge	|	5.9	|	2022-09-19	|	19:42:57.375`\		
  `New Zeland	|	south of Africa	|	5.2	|	2022-09-17	|	08:25:55.818`\		
  `New Zeland	|	29 km W of Ovalle, Chile	|	4.5	|	2022-09-15	|	22:29:40.348`\		
  `New Zeland	|	12 km WSW of Ovalle, Chile	|	4.7	|	2022-09-15	|	01:41:14.093`\

- Input:\
  `El Salvador, 0`

* Output:\
  `No Earthquakes were recorded past 0 days`
