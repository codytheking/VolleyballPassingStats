//
//  PlayerTableViewCell.swift
//  VolleyballPassingStats
//
//  Created by Cody King on 4/26/20.
//  Copyright © 2020 Cody J. King. All rights reserved.
//

import UIKit

class PlayerTableViewCell: UITableViewCell {

    // MARK: Properties
    
    @IBOutlet weak var nameTextField: UITextField!
    @IBOutlet weak var scoringButtonsControl: ScoringButtonsControl!
    @IBOutlet weak var statsLabel: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

//    override func setSelected(_ selected: Bool, animated: Bool) {
//        super.setSelected(selected, animated: animated)
//
//        // Configure the view for the selected state
//    }
    
    

}
