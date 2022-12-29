#![allow(dead_code)]

use std::fs;

mod day19;

fn main() {
    let str = fs::read_to_string("in/in19_ex.txt").unwrap();
    // let str = fs::read_to_string("in/in19.txt").unwrap();
    day19::day19_2(&str);
}