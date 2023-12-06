use std::cmp::{max, min};
use std::collections::HashMap;
use std::i64::MAX;
use std::ops::Shl;

pub fn day6_1(str: &str) {

    let lines = str.lines().collect::<Vec<&str>>();

    let mut times:Vec<i32> = vec![];
    let mut max_distances:Vec<i32> = vec![];

    let times_split=lines[0].split(" ").filter(|n| !n.trim().is_empty()).collect::<Vec<&str>>();
    let distances_split=lines[1].split(" ").filter(|n| !n.trim().is_empty()).collect::<Vec<&str>>();

    for time in 1..times_split.len() {
        times.push(times_split[time].parse::<i32>().unwrap());
    }
    for distance in 1..distances_split.len() {
        max_distances.push(distances_split[distance].parse::<i32>().unwrap());
    }

    let mut prod = 1;
    for i in 0..times.len() {
        let mut count = 0;
        let time = times[i];
        let to_beat = max_distances[i];
        for chargeup in 1..time {
            let result = (time - chargeup) * chargeup;
            if result > to_beat {
                count += 1;
            }
        }
        prod *= count;
    }

    println!("{}", prod);
}
