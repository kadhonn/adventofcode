use std::collections::{HashMap, HashSet};

pub fn day23_2(str: &str) {
    let mut field = parse_field(str);

    let mut dirs_i = 0;
    let dirs = [
        [(-1, -1), (0, -1), (1, -1)],
        [(-1, 1), (0, 1), (1, 1)],
        [(-1, -1), (-1, 0), (-1, 1)],
        [(1, -1), (1, 0), (1, 1)],
    ];

    let mut i = 1;
    loop {
        let mut proposed: HashMap<(i32, i32), Option<(i32, i32)>> = HashMap::new();

        for elf in &field {
            let elf_proposed = get_proposed_move(elf.clone(), &field, &dirs, dirs_i);
            if let Some(elf_move) = elf_proposed {
                if proposed.contains_key(&elf_move) {
                    proposed.insert(elf_move, None);
                } else {
                    proposed.insert(elf_move, Some(elf.clone()));
                }
            }
        }

        let mut moved = false;
        for proposed_move in proposed.keys() {
            let elf_moved = proposed[proposed_move];
            if let Some(elf) = elf_moved {
                field.remove(&elf);
                field.insert(proposed_move.clone());
                moved = true;
            }
        }
        if !moved {
            break;
        }

        dirs_i += 1;
        i += 1;
    }

    println!("{}", i);
}

fn get_proposed_move(elf: (i32, i32), field: &HashSet<(i32, i32)>, dirs: &[[(i32, i32); 3]; 4], dirs_i: usize) -> Option<(i32, i32)> {
    let mut found_elf = false;
    for dir in dirs.iter().flatten() {
        let pos = (elf.0 + dir.0, elf.1 + dir.1);
        if field.contains(&pos) {
            found_elf = true;
            break;
        }
    }

    if !found_elf {
        return None;
    }

    'outer: for i in 0..dirs.len() {
        let dirs = dirs[(i + dirs_i) % dirs.len()];
        for dir in dirs {
            let pos = (elf.0 + dir.0, elf.1 + dir.1);
            if field.contains(&pos) {
                continue 'outer;
            }
        }
        let pos = (elf.0 + dirs[1].0, elf.1 + dirs[1].1);
        return Some(pos);
    }

    None
}

fn parse_field(str: &str) -> HashSet<(i32, i32)> {
    let mut field = HashSet::new();
    let input: Vec<Vec<char>> = str.lines().map(|l| { l.chars().collect() }).collect();

    for y in 0..input.len() {
        for x in 0..input[y].len() {
            if input[y][x] == '#' {
                field.insert((x as i32, y as i32));
            }
        }
    }

    field
}