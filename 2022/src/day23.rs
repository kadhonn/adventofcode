use std::collections::{HashMap, HashSet};

pub fn day23_1(str: &str) {
    let mut field = parse_field(str);

    let mut dirs_i = 0;
    let dirs = [
        [(-1, -1), (0, -1), (1, -1)],
        [(-1, 1), (0, 1), (1, 1)],
        [(-1, -1), (-1, 0), (-1, 1)],
        [(1, -1), (1, 0), (1, 1)],
    ];

    for _ in 0..10 {
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

        for proposed_move in proposed.keys() {
            let elf_moved = proposed[proposed_move];
            if let Some(elf) = elf_moved {
                field.remove(&elf);
                field.insert(proposed_move.clone());
            }
        }

        dirs_i += 1;
    }

    let mut min_x = 100000;
    let mut max_x = -100000;
    let mut min_y = 100000;
    let mut max_y = -100000;
    for elf in &field {
        min_x = min_x.min(elf.0);
        max_x = max_x.max(elf.0);
        min_y = min_y.min(elf.1);
        max_y = max_y.max(elf.1);
    }

    let field_size = (max_x - min_x + 1) * (max_y - min_y + 1);
    let empty_space = field_size - field.len() as i32;

    println!("{}", empty_space);
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