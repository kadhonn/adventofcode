#![allow(dead_code)]

extern crate core;

use std::fs;

mod day24;

fn main() {
    // let str = fs::read_to_string("in/in24_ex.txt").unwrap();
    let str = fs::read_to_string("in/in24.txt").unwrap();
    day24::day24_1(&str);
}