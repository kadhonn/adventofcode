#![allow(dead_code)]

extern crate core;

use std::fs;
use std::time::Instant;

mod day16;

fn main() {
    let before = Instant::now();
    // let str = fs::read_to_string("in/in16_ex.txt").unwrap();
    let str = fs::read_to_string("in/in16.txt").unwrap();
    day16::day16(&str.trim());
    println!("took {:?}", before.elapsed());
}