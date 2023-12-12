#![allow(dead_code)]

extern crate core;

use std::fs;
use std::time::Instant;

mod day12;

fn main() {
    let before = Instant::now();
    // let str = fs::read_to_string("in/in12_ex.txt").unwrap();
    let str = fs::read_to_string("in/in12.txt").unwrap();
    day12::day12(&str.trim());
    println!("took {:?}", before.elapsed());
}