#![allow(dead_code)]

use std::fs;

mod day5;

fn main() {
    // let str = fs::read_to_string("in/in5_ex.txt").unwrap();
    let str = fs::read_to_string("in/in5.txt").unwrap();
    day5::day5_2(&str);
}