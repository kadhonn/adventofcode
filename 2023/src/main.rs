#![allow(dead_code)]

extern crate core;

use std::fs;

mod day4;

fn main() {
    // let str = fs::read_to_string("in/in4_ex.txt").unwrap();
    let str = fs::read_to_string("in/in4.txt").unwrap();
    day4::day4_1(&str.trim());
}