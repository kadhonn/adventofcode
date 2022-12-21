#![allow(dead_code)]

use std::fs;

mod day16;

fn main() {
    // let str = fs::read_to_string("in/in16_ex.txt").unwrap();
    let str = fs::read_to_string("in/in16.txt").unwrap();
    day16::day16_2(&str.trim());
}