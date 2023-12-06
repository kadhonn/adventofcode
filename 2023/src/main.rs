#![allow(dead_code)]

extern crate core;

use std::fs;
use std::time::Instant;

mod day6;

fn main() {
    let before = Instant::now();
    // let str = fs::read_to_string("in/in6_ex.txt").unwrap();
    let str = fs::read_to_string("in/in6.txt").unwrap();
    day6::day6_2(&str.trim());
    println!("took {:?}", before.elapsed());
}