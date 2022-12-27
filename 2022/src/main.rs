#![allow(dead_code)]

use std::fs;

mod day18;

fn main() {
    // let str = fs::read_to_string("in/in18_ex.txt").unwrap();
    let str = fs::read_to_string("in/in18.txt").unwrap();
    day18::day18_2(&str);
}