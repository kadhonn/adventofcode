use regex::Regex;

pub fn day15_1(str: &str) {
    let sensors = parse_input(str);

    let ranges = get_ranges(&sensors, 2000000);

    let result = count_ranges(&ranges);

    println!("{result}");
}

static MAX: i64 = 4000000;

pub fn day15_2(str: &str) {
    let sensors = parse_input(str);

    for i in 0..=MAX {
        let ranges = get_ranges(&sensors, i);
        let result = count_ranges_2(&ranges);
        if result != MAX {
            println!("{i} {result}");
        }
    }
}

fn count_ranges_2(ranges: &Vec<(i64, i64)>) -> i64 {
    let mut ranges = ranges.clone();
    ranges.sort_by(|r1, r2| { r1.0.cmp(&r2.0) });

    let mut count = 0;
    let mut current_x: i64 = 0;

    for range in ranges {
        let start = current_x.max(range.0);
        if start > current_x {
            println!("{current_x} {start}");
        }
        let end = MAX.min(range.1);
        if end > start {
            count += end - start;
        }
        current_x = current_x.max(end);
        if current_x == MAX {
            break;
        }
    }

    count
}

fn count_ranges(ranges: &Vec<(i64, i64)>) -> i64 {
    let mut ranges = ranges.clone();
    ranges.sort_by(|r1, r2| { r1.0.cmp(&r2.0) });

    let mut count = 0;
    let mut current_x: i64 = -10000000;

    for range in ranges {
        let start = current_x.max(range.0);
        let end = range.1;
        if end > start {
            count += end - start;
        }
        current_x = current_x.max(end);
    }

    count
}

fn get_ranges(sensors: &Vec<(i64, i64, i64, i64)>, line: i64) -> Vec<(i64, i64)> {
    let mut ranges = vec!();

    for sensor in sensors {
        let sensor_range = (sensor.0 - sensor.2).abs() + (sensor.1 - sensor.3).abs();

        let y_distance = (sensor.1 - line).abs();

        let x_range = sensor_range - y_distance;

        if x_range >= 0 {
            ranges.push((sensor.0 - x_range, sensor.0 + x_range));
        }
    }

    ranges
}

fn parse_input(str: &str) -> Vec<(i64, i64, i64, i64)> {
    let regex = Regex::new(r"Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)").unwrap();

    let mut sensors = vec![];

    for line in str.lines() {
        let sensor = regex.captures_iter(line).next().unwrap();
        sensors.push((
            sensor[1].parse().unwrap(),
            sensor[2].parse().unwrap(),
            sensor[3].parse().unwrap(),
            sensor[4].parse().unwrap()));
    }

    sensors
}
