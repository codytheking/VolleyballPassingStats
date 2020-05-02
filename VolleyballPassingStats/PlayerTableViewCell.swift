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
    //@IBOutlet weak var scoringButtonsControl: ScoringButtonsControl!
    @IBOutlet weak var statsLabel: UILabel!
    @IBOutlet weak var scoringBtn0: UIButton!
    @IBOutlet weak var scoringBtn1: UIButton!
    @IBOutlet weak var scoringBtn2: UIButton!
    @IBOutlet weak var scoringBtn3: UIButton!
    
    // model (Player class)
    var name: String?
    
    // the delegate, remember to set to weak to prevent cycles
    weak var delegate: PlayerTableViewCellDelegate?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
        name = nameTextField.text
        
        // Add action to perform when the button is tapped
        self.scoringBtn0.addTarget(self, action: #selector(scoringButtonTapped(_:)), for: .touchUpInside)
        self.scoringBtn1.addTarget(self, action: #selector(scoringButtonTapped(_:)), for: .touchUpInside)
        self.scoringBtn2.addTarget(self, action: #selector(scoringButtonTapped(_:)), for: .touchUpInside)
        self.scoringBtn3.addTarget(self, action: #selector(scoringButtonTapped(_:)), for: .touchUpInside)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
        
    @IBAction func scoringButtonTapped(_ sender: UIButton) {
        // ask the delegate (in most case, its the view controller) to
        // call the function 'scoringButtonTappedFor' on itself.
        
        // TODO Fix this!
        
        
        // make sure the sender is a button
        let btn = sender
        
        // make sure the button's superview is a stack view
        guard let stack = btn.superview as? UIStackView else { return }
        
        // get the array of arranged subviews
        let theArray = stack.arrangedSubviews
        
        // get the "index" of the tapped button
        let idx = theArray.firstIndex(of: btn)!
        
        //if let name = name,
        if let _ = delegate {
            self.delegate?.playerTableViewCell(self, scoringButtonTappedFor: idx)
        }
    }
    
}

// Only class object can conform to this protocol (struct/enum can't)
protocol PlayerTableViewCellDelegate: AnyObject {
  func playerTableViewCell(_ playerTableViewCell: PlayerTableViewCell, scoringButtonTappedFor value: Int)
}
