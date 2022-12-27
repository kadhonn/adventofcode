use std::collections::{HashSet};

pub fn day18_1(str: &String) {
    let cubes = parse_input(str);

    let cube_set: HashSet<(i32, i32, i32)> = HashSet::from_iter(cubes.iter().cloned());

    let mut count = 0;
    for cube in cubes {
        for dir in [
            (1, 0, 0),
            (-1, 0, 0),
            (0, 1, 0),
            (0, -1, 0),
            (0, 0, 1),
            (0, 0, -1)] {
            let cube_side = (cube.0 + dir.0, cube.1 + dir.1, cube.2 + dir.2);
            if !cube_set.contains(&cube_side) {
                count += 1;
            }
        }
    }

    println!("{count}");
}

pub fn day18_2(str: &String) {
    let cubes = parse_input(str);

    let cube_set: HashSet<(i32, i32, i32)> = HashSet::from_iter(cubes.iter().cloned());

    let air_set: HashSet<(i32, i32, i32)> = flood_air_set(&cubes, &cube_set);

    let mut count = 0;
    for cube in cubes {
        for dir in [
            (1, 0, 0),
            (-1, 0, 0),
            (0, 1, 0),
            (0, -1, 0),
            (0, 0, 1),
            (0, 0, -1)] {
            let cube_side = (cube.0 + dir.0, cube.1 + dir.1, cube.2 + dir.2);
            if air_set.contains(&cube_side) {
                count += 1;
            }
        }
    }

    println!("{count}");
}

fn flood_air_set(cubes: &Vec<(i32, i32, i32)>, cube_set: &HashSet<(i32, i32, i32)>) -> HashSet<(i32, i32, i32)> {
    let mut air_set = HashSet::new();

    let mut max_x = -10000;
    let mut min_x = 10000;
    let mut max_y = -10000;
    let mut min_y = 10000;
    let mut max_z = -10000;
    let mut min_z = 10000;
    for cube in cubes {
        max_x = max_x.max(cube.0);
        min_x = min_x.min(cube.0);
        max_y = max_y.max(cube.1);
        min_y = min_y.min(cube.1);
        max_z = max_z.max(cube.2);
        min_z = min_z.min(cube.2);
    }

    max_x += 1;
    min_x -= 1;
    max_y += 1;
    min_y -= 1;
    max_z += 1;
    min_z -= 1;

    let mut to_do = vec![];

    to_do.push((min_x, min_y, min_z));
    while !to_do.is_empty() {
        let next = to_do.pop().unwrap();
        if next.0 < min_x || next.0 > max_x || next.1 < min_y || next.1 > max_y || next.2 < min_z || next.2 > max_z {
            continue;
        }
        if cube_set.contains(&next) {
            continue;
        }
        if air_set.contains(&next) {
            continue;
        }
        air_set.insert(next);

        for dir in [
            (1, 0, 0),
            (-1, 0, 0),
            (0, 1, 0),
            (0, -1, 0),
            (0, 0, 1),
            (0, 0, -1)] {
            let cube_side = (next.0 + dir.0, next.1 + dir.1, next.2 + dir.2);
            to_do.push(cube_side);
        }
    }

    air_set
}

fn parse_input(str: &String) -> Vec<(i32, i32, i32)> {
    let mut cubes: Vec<(i32, i32, i32)> = vec![];

    for cube in str.trim().lines() {
        let split: Vec<&str> = cube.split(",").collect();
        cubes.push((split[0].parse().unwrap(), split[1].parse().unwrap(), split[2].parse().unwrap()));
    }

    cubes
}